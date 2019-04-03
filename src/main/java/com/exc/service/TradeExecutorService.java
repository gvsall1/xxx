package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import com.exc.domain.RateCalculator;
import com.exc.domain.TradeCalculator;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.order.OrderPair;
import com.exc.repository.CurrencyPairRepository;
import com.exc.repository.order.OrderPairRepository;
import com.exc.repository.order.OrderPairRepositoryFactory;
import com.exc.service.dto.PreparedExDTO;
import com.exc.service.dto.remote.KeysRequestDTO;
import com.exc.service.dto.remote.KeysResponseDTO;
import com.exc.service.errors.OrderPairExecutionException;
import com.exc.service.remote.TxService;
import com.exc.service.remote.UserService;
import com.exc.service.sync.OrderSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Proxy between external currency nodes, and local orders/txs
 * Responsible to execute orders
 */
@Service
@Transactional
public class TradeExecutorService {
    private final Logger log = LoggerFactory.getLogger(TradeExecutorService.class);
    private OrderPairRepositoryFactory orderPairRepositoryFactory;
    private CurrencyPairRepository currencyPairRepository;
    private OrderSyncService orderSyncService;
    private OrderPairService orderPairService;
    private TradesService tradesService;
    private UserService userService;
    private TxService txService;

    public TradeExecutorService(OrderPairRepositoryFactory orderPairRepositoryFactory, CurrencyPairRepository currencyPairRepository, OrderSyncService orderSyncService, OrderPairService orderPairService, TradesService tradesService, UserService userService, TxService txService) {
        this.orderPairRepositoryFactory = orderPairRepositoryFactory;
        this.currencyPairRepository = currencyPairRepository;
        this.orderSyncService = orderSyncService;
        this.orderPairService = orderPairService;
        this.tradesService = tradesService;
        this.userService = userService;
        this.txService = txService;
    }

    @Autowired
    public void setOrderPairService(@Lazy OrderPairService orderPairService) {
        this.orderPairService = orderPairService;
    }

    @Autowired
    public void setTradesService(@Lazy TradesService tradesService) {
        this.tradesService = tradesService;
    }

    /**
     * Create Tx records in DB, and execute order.
     * If order would not fit in full, it will be updated with new value.
     * Sends personal user notifications. Manage order RAM locks.
     */
    @Async
    public void executeOrderAsync(Long exId, Long pairId) {
        executeOrder(exId, pairId);
    }


    public void executeOrder(Long exId, Long pairId) {
        log.info("Executing order: {}", exId);

        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        CurrencyName buy = pair.getBuy().getCurrencyName(), sell = pair.getSell().getCurrencyName();
        OrderPairRepository repository = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN);
        OrderPair newOrder = repository.getOne(exId);
        if (newOrder.getExecutions() == null || newOrder.getExecutions().size() == 0) {
            log.error("Executions empty orderId {} pairId {}", exId, pairId);
            throw new OrderPairExecutionException("Trade does not have execute pair!");
        }
        Set<OrderPair> orderPairSet = newOrder.getExecutions();
        Set<Long> all = new HashSet<>();
        all.addAll(orderPairSet.stream().map(op -> op.getUserId()).collect(Collectors.toList()));
        all.add(newOrder.getUserId());
        Map<Long, KeysResponseDTO> pKeys = userService.getKeys(new KeysRequestDTO(all, buy, sell));

        List<Long> repeatProcessOrders = new ArrayList<>(newOrder.sizeOfExecutions());
        // ram lock
        List<Long> unSyncOrders = new ArrayList<>(newOrder.sizeOfExecutions());
        List<PreparedExDTO> preparedTransactions = new ArrayList<>(newOrder.sizeOfExecutions() * 2);
        TradeCalculator tc = new TradeCalculator(newOrder.getValue());
        boolean executed = false;

        for (OrderPair orderI : orderPairSet) {
            log.info("executing orderId {} with {} pair {}", newOrder.getId(), orderI.getId(), orderI.getPair().getId());
            if (!executed) {
                tc.calcTrade(orderI.getValue());
                RateCalculator rc = new RateCalculator(orderI, newOrder, tc.getDiff());

                PreparedExDTO pex = new PreparedExDTO(
                    buy, pKeys.get(orderI.getUserId()),
                    sell, pKeys.get(newOrder.getUserId()),
                    rc.getBuyValue(), rc.getSellValue(),
                    rc.getBuyOrder().getId(), rc.getSellOrder().getId());


                if (pex != null) {
                    preparedTransactions.add(pex);
                    if (tc.isExExtra()) {
                        executed = true;
                        unSyncOrders.add(orderI.getId());
                        if (!tc.getDiff().equals(orderI.getValue())) {
                            Long reprocess = orderPairService.decouplePartialOrder(newOrder.getId(), orderI.getId(), pairId, orderI.getValue().subtract(tc.getDiff()));
                            repeatProcessOrders.add(reprocess);
                        }

                        continue;
                    }
                }
            } else {
                unSyncOrders.add(orderI.getId());
            }
        }


        txService.processOrderTransactions(preparedTransactions);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                orderSyncService.unlockIds(pairId, unSyncOrders);

                try {
                    tradesService.reProcessExistingOrderAsync(repeatProcessOrders, pairId, OrderStatusType.NEW);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

        });

    }


}
