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
import com.exc.service.errors.FailedValidateOrderInput;
import com.exc.service.sync.OrderSyncService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class TradesServiceTest extends AbstractServiceTest {
    @Autowired
    OrderPairService orderPairService;
    @Autowired
    CurrencyPairRepository currencyPairRepository;
    @MockBean
    OrderSyncService orderSyncService;
    @MockBean
    TradeExecutorService tradeExecutorService;
    @MockBean
    OrderPairEthBtcOpenRepository orderPairEthBtcOpenRepository;

    @Autowired
    OrderPairRepositoryFactory orderPairRepositoryFactory;
    @Autowired
    TradesService tradesService;
    @Autowired
    EntityFactory entityFactory;
    @MockBean
    ClientWebSocketService clientWebSocketService;

    OrderPair firstOrder;
    OrderPair secondOrder;
    CurrencyPair pair;
    CurrencyName buy = CurrencyName.ETH, sell = CurrencyName.BTC;


    @Before
    @Transactional
    public void init() throws InterruptedException {
        doNothing().when(clientWebSocketService).sendPersonalOrderNotification(any(), any());
        pair = currencyPairRepository.findByBuyCurrencyNameAndSellCurrencyName(buy, sell);
        firstOrder = entityFactory.makeOrder(buy, sell, OrderStatusType.NEW, null);
        firstOrder.setId(1l);
        firstOrder.setPair(pair);
        firstOrder.setStatus(OrderStatusType.NEW);
        firstOrder.setType(OrderType.BUY);
        firstOrder.setValue(new BigInteger("5"));
        firstOrder.setRate(new BigDecimal("1.1"));
        // firstOrder = orderPairService.save(firstOrder, false, "user");
        secondOrder = entityFactory.makeOrder(buy, sell, OrderStatusType.NEW, null);
        secondOrder.setId(2l);
        secondOrder.setPair(pair);
        secondOrder.setStatus(OrderStatusType.NEW);
        secondOrder.setType(OrderType.SELL);
        secondOrder.setValue(new BigInteger("5"));
        secondOrder.setRate(new BigDecimal("1.1"));

        when(orderSyncService.tryLock(any(), any())).thenReturn(true);
        doNothing().when(tradeExecutorService).executeOrderAsync(anyLong(), anyLong());
        when(orderPairEthBtcOpenRepository
            .findByTypeAndRateLessThanEqualAndUserIdNot(any(), any(), any()))
            .thenReturn(Arrays.asList((OrderPairEthBtcOpen) secondOrder));
        when(orderPairEthBtcOpenRepository
            .findByTypeAndRateGreaterThanEqualAndUserIdNot(any(), any(), any()))
            .thenReturn(Arrays.asList((OrderPairEthBtcOpen) secondOrder));
    }


    @Test(expected = FailedValidateOrderInput.class)
    public void testOrderStatusDirectExecution() throws InterruptedException {
        firstOrder.setStatus(OrderStatusType.OPEN);
        secondOrder.setStatus(OrderStatusType.OPEN);

        firstOrder.setStatus(OrderStatusType.IN_PROCESS);
        tradesService.processNewOrder(firstOrder, secondOrder);
    }

    @Test
    public void testOrderDirectExecution() throws InterruptedException {
        tradesService.processNewOrder(firstOrder, secondOrder);
        verify(tradeExecutorService, times(1)).executeOrderAsync(any(), any());
    }

    @Test
    public void testOrderInDirectExecution() throws InterruptedException {
        tradesService.processNewOrder(firstOrder, null);
        verify(tradeExecutorService, times(1)).executeOrderAsync(any(), any());
        verify(orderPairEthBtcOpenRepository, times(1)).findByTypeAndRateLessThanEqualAndUserIdNot(any(), any(), any());
    }

    @Test
    public void testOrderInDirectExecutionReverse() throws InterruptedException {
        firstOrder.setType(OrderType.SELL);
        secondOrder.setType(OrderType.BUY);
        tradesService.processNewOrder(firstOrder, null);
        verify(tradeExecutorService, times(1)).executeOrderAsync(any(), any());
        verify(orderPairEthBtcOpenRepository, times(1)).findByTypeAndRateGreaterThanEqualAndUserIdNot(any(), any(), any());
    }

}
