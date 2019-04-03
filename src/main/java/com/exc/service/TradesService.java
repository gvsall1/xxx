package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.repository.CurrencyPairRepository;
import com.exc.repository.order.OrderPairRepository;
import com.exc.repository.order.OrderPairRepositoryFactory;
import com.exc.service.errors.FailedValidateOrderInput;
import com.exc.service.sync.OrderSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * internal (helper)service, responsible for preparing orders for execution, and submit execution.
 * Should not be used out of service layer.
 */
@Service
@Transactional
public class TradesService {
    private final Logger log = LoggerFactory.getLogger(TradesService.class);
    private  TradeExecutorService tradeExecutorService;
    private final OrderSyncService orderSyncService;
    private final OrderPairRepositoryFactory orderPairRepositoryFactory;
    private final CurrencyPairRepository currencyPairRepository;

    public TradesService( OrderSyncService orderSyncService, OrderPairRepositoryFactory orderPairRepositoryFactory, CurrencyPairRepository currencyPairRepository) {
        this.orderSyncService = orderSyncService;
        this.orderPairRepositoryFactory = orderPairRepositoryFactory;
        this.currencyPairRepository = currencyPairRepository;
    }

    @Autowired
    public void setTradeExecutorService(@Lazy TradeExecutorService tradeExecutorService) {
        this.tradeExecutorService = tradeExecutorService;
    }

    /**
     * @param ids
     * @param pairId
     * @param orderStatusType
     */
    @Async
    public void reProcessExistingOrderAsync(List<Long> ids, Long pairId, OrderStatusType orderStatusType) throws InterruptedException {
        if (ids == null || ids.size() == 0) {
            return;
        }
        log.info("Request to reprocess updated orders for pair : {}, order: {}", pairId, ids.get(0));
        CurrencyPair pair = currencyPairRepository.getOne(pairId);
        OrderPairRepository newRepo = orderPairRepositoryFactory.getOrderPairRepo(pair.getBuy().getCurrencyName(), pair.getSell().getCurrencyName(), orderStatusType);

        for (Long newId : ids) {
            OrderPair orderPair = newRepo.getOne(newId);
            processNewOrder(orderPair, null);
        }

    }

/**
 * if execute null, it will find all orders that fit by rate, and execute order with NEW(only) status
 * There is no validation for rate and value, in case execute is not null.
 *
 * @param orderPair
 * @param execute   leave null to ignore direct execution
 * @return true in case execute were accepted
 */
    /**
     * @param orderPair
     * @param execute
     * @return
     */

    public boolean processNewOrder(OrderPair orderPair, @Nullable OrderPair execute) throws InterruptedException {
        if (orderPair.getStatus() != OrderStatusType.NEW) {
            log.error("Can process orders status NEW only");
            throw new FailedValidateOrderInput("Wrong status in order/pair: " + orderPair.getId() + "/" + orderPair.getPair().getId());
        }
        /*
                        //means not enough balance of the root user's order
        boolean mainBalanceFailed = false;

        Set<Long> lockedUsers = new HashSet<>();

                  try {
              } catch (NotEnoughBalanceException ex) {
                    //todo: lock orders for currency specific user ?
                    lockedUsers.add(ex.getUserId());
                    if (lockedUsers.contains(newOrder.getUserId())) {
                        mainBalanceFailed = true;
                        break;
                    }
                }
                   if (lockedUsers.size() > 0) {
            if (mainBalanceFailed) {
                orderPairService.cancel(newOrder.getId(), pairId, pKeys.get(newOrder.getUserId()).getPrivateKey());
            } else {
                orderPairService.cancelOrders(lockedUsers, pairId, pKeys);
            }

        }
                */
        boolean isLockedOnce = false;
        CurrencyName buy = orderPair.getPair().getBuy().getCurrencyName(), sell = orderPair.getPair().getSell().getCurrencyName();
        List<OrderPair> preExecute = execute == null ? searchOrdersForExecution(orderPair) : Arrays.asList(execute);
        Set<OrderPair> executionPairs = new HashSet<>(preExecute.size());

        BigInteger sum = new BigInteger("0");

        for (OrderPair fitPair : preExecute) {
            if (orderSyncService.tryLock(fitPair.getId(), orderPair.getPair().getId())) {
                isLockedOnce = true;
                sum = sum.add(fitPair.getValue());

                executionPairs.add(fitPair);
                fitPair.setStatus(OrderStatusType.IN_PROCESS);

                if (sum.compareTo(orderPair.getValue()) >= 0) {
                    break;
                }
            }
        }

        if (executionPairs.size() > 0) {
            orderPair.setStatus(OrderStatusType.IN_PROCESS);
            if (orderPair.getExecutions().size() > 0) {
                executionPairs.forEach(ex -> orderPair.addExecution(ex));
            } else {
                orderPair.setExecutions(executionPairs);
            }
            orderPairRepositoryFactory.getOrderPairRepo(buy, sell, orderPair.getStatus()).save(orderPair);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    tradeExecutorService.executeOrderAsync(orderPair.getId(), orderPair.getPair().getId());
                }

            });


        } else {
            orderPair.setStatus(OrderStatusType.OPEN);
            orderPairRepositoryFactory.getOrderPairRepo(buy, sell, orderPair.getStatus()).save(orderPair);
        }

        processTradeQuote(orderPair);
        return isLockedOnce;
    }


    private void processTradeQuote(OrderPair orderPair) {
     /*   StringBuilder ch = new StringBuilder();
        ch.append('q');
        ch.append(orderPair.getPair().getBuy().getCurrencyName().name());
        ch.append('-');
        ch.append(orderPair.getPair().getSell().getCurrencyName().name());
       redisTemplate.convertAndSend(ch,);*/
    }


    /**
     * search in existing OPEN orders.
     * //TODO: paginate
     *
     * @param orderPair
     * @return
     */
    private List<OrderPair> searchOrdersForExecution(OrderPair orderPair) {
        List<OrderPair> pairs = null;
        //search to execute order
        CurrencyName buy = orderPair.getPair().getBuy().getCurrencyName();
        CurrencyName sell = orderPair.getPair().getSell().getCurrencyName();
        OrderType type = OrderType.BUY;
        if (orderPair.getType().equals(OrderType.BUY)) {
            type = OrderType.SELL;
            pairs = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN).
                findByTypeAndRateLessThanEqualAndUserIdNot(type, orderPair.getRate(), orderPair.getUserId());
        } else {
            pairs = orderPairRepositoryFactory.getOrderPairRepo(buy, sell, OrderStatusType.OPEN).
                findByTypeAndRateGreaterThanEqualAndUserIdNot(type, orderPair.getRate(), orderPair.getUserId());
        }

        return pairs;
    }
}
