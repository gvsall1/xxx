package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import com.exc.domain.EntityFactory;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.domain.order.OrderPairEthBtcOpen;
import com.exc.repository.CurrencyPairRepository;
import com.exc.repository.order.OrderPairEthBtcOpenRepository;
import com.exc.repository.order.OrderPairRepositoryFactory;
import com.exc.service.dto.remote.KeysResponseDTO;
import com.exc.service.errors.OrderPairExecutionException;
import com.exc.service.sync.OrderSyncService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class TradeExecutorServiceTest extends AbstractServiceTest {
    @MockBean
    OrderPairService orderPairService;
    @Autowired
    CurrencyPairRepository currencyPairRepository;
    @MockBean
    OrderSyncService orderSyncService;
    @Autowired
    TradeExecutorService tradeExecutorService;
    @MockBean
    private OrderPairEthBtcOpenRepository orderPairEthBtcOpenRepository;


    @Autowired
    OrderPairRepositoryFactory orderPairRepositoryFactory;
    @MockBean
    TradesService tradesService;
    @Autowired
    EntityFactory entityFactory;
    @MockBean
    ClientWebSocketService clientWebSocketService;


    OrderPair firstOrder;
    OrderPair secondOrder;
    long decoupleId = 10050l;
    CurrencyPair pair;
    CurrencyName buy = CurrencyName.ETH, sell = CurrencyName.BTC;


    @Before
    @Transactional
    public void init() throws InterruptedException {
        doNothing().when(clientWebSocketService).sendPersonalOrderNotification(any(), any());
        pair = currencyPairRepository.findByBuyCurrencyNameAndSellCurrencyName(buy, sell);
        resetOrders();

        when(orderSyncService.tryLock(any(), any())).thenReturn(true);

        when(orderPairEthBtcOpenRepository
            .getOne(firstOrder.getId()))
            .thenReturn((OrderPairEthBtcOpen) firstOrder);

        when(orderPairEthBtcOpenRepository
            .getOne(secondOrder.getId()))
            .thenReturn((OrderPairEthBtcOpen) secondOrder);

        when(orderPairEthBtcOpenRepository
            .save((OrderPairEthBtcOpen) secondOrder))
            .thenReturn((OrderPairEthBtcOpen) secondOrder);

        doNothing().when(tradesService).reProcessExistingOrderAsync(anyList(), anyLong(), any());

        when(orderPairService.decouplePartialOrder(any(), anyLong(), any(), any())).thenReturn(decoupleId);

    }

    private void resetOrders() {
        if (firstOrder == null)
            firstOrder = entityFactory.makeOrder(buy, sell, OrderStatusType.NEW, null);

        firstOrder.setId(1l);
        firstOrder.setPair(pair);
        firstOrder.setStatus(OrderStatusType.OPEN);
        firstOrder.setType(OrderType.BUY);
        firstOrder.setValue(new BigInteger("5"));
        firstOrder.setRate(new BigDecimal("1.1"));
        if (secondOrder == null)
            secondOrder = entityFactory.makeOrder(buy, sell, OrderStatusType.NEW, null);
        secondOrder.setId(2l);
        secondOrder.setPair(pair);
        secondOrder.setStatus(OrderStatusType.OPEN);
        secondOrder.setType(OrderType.SELL);
        secondOrder.setValue(new BigInteger("5"));
        secondOrder.setRate(new BigDecimal("1.1"));
        firstOrder.addExecution(secondOrder);


        Map<Long, KeysResponseDTO> keys = new HashMap<>();
        when(userService.getKeys(any())).thenReturn(keys);
        keys.put(firstOrder.getUserId(), new KeysResponseDTO());
        keys.put(secondOrder.getUserId(), new KeysResponseDTO());
    }

    @Test
    public void executeEq() {
        tradeExecutorService.executeOrder(firstOrder.getId(), pair.getId());
        verify(txService, times(1)).processOrderTransactions(anyList());

    }

    @Test
    public void executeLs() {
        secondOrder.setValue(new BigInteger("1"));
        tradeExecutorService.executeOrder(firstOrder.getId(), pair.getId());
        verify(txService, times(1)).processOrderTransactions(anyList());
    }

    @Test
    public void executeGr() {
        firstOrder.setValue(new BigInteger("1"));
        tradeExecutorService.executeOrder(firstOrder.getId(), pair.getId());
        verify(txService, times(1)).processOrderTransactions(anyList());
    }

  /* balance check migrated to the tx microservice, in case user get into low balance, user will be punished
  @Test
    public void testLowBalanceBuy() {
        firstOrder.setValue(new BigInteger("1000000000"));
        secondOrder.setValue(new BigInteger("2000000000"));
        tradeExecutorService.executeOrder(firstOrder.getId(), pair.getId());
        verify(orderPairService, times(1)).cancel(any(), any(), any());
    }

    @Test
    public void testLowBalanceSell() {
        firstOrder.setValue(new BigInteger("1000000"));
        secondOrder.setValue(new BigInteger("1000000"));
        firstOrder.removeExecution(secondOrder);
        secondOrder.addExecution(firstOrder);


        tradeExecutorService.executeOrder(secondOrder.getId(), pair.getId());
        verify(orderPairService, times(0)).cancel(any(), any(), any());
        verify(orderPairService, times(1)).cancelOrders(any(), any(), any());
    }
*/

    @Test(expected = OrderPairExecutionException.class)
    public void testEmptyEx() {
        firstOrder.setValue(new BigInteger("1"));
        secondOrder.setValue(new BigInteger("2"));
        firstOrder.setExecutions(Collections.EMPTY_SET);
        tradeExecutorService.executeOrder(secondOrder.getId(), pair.getId());
    }


}
