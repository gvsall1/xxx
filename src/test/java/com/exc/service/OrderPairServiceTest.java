package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import com.exc.domain.EntityFactory;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.repository.CurrencyPairRepository;
import com.exc.repository.order.OrderPairRepositoryFactory;
import com.exc.service.dto.OrderPairDTO;
import com.exc.service.dto.remote.KeysRequestDTO;
import com.exc.service.dto.remote.KeysResponseDTO;
import com.exc.service.errors.OrderPairCancelException;
import com.exc.service.errors.OrderPairExecutionException;
import com.exc.service.errors.UserNotFound;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class OrderPairServiceTest extends AbstractServiceTest {

    @Autowired
    OrderPairService orderPairService;
    @Autowired
    CurrencyPairRepository currencyPairRepository;
    @MockBean
    OrderPairFeeService orderPairFeeService;
    @Autowired
    OrderPairRepositoryFactory orderPairRepositoryFactory;
    @MockBean
    TradesService tradesService;
    @Autowired
    EntityFactory entityFactory;
    OrderPairDTO firstOrder;
    OrderPairDTO secondOrder;
    CurrencyPair pair;
    CurrencyName buy = CurrencyName.ETH, sell = CurrencyName.BTC;
    @MockBean
    ClientWebSocketService clientWebSocketService;

    @Before
    @Transactional
    public void init() throws InterruptedException {
        doNothing().when(clientWebSocketService).sendQuote(any());
        doNothing().when(clientWebSocketService).sendPersonalOrderNotification(any(), any());

        pair = currencyPairRepository.findByBuyCurrencyNameAndSellCurrencyName(buy, sell);

        when(orderPairFeeService.withdrawOrderFee(any(), any(), anyString())).thenReturn(1l);
        when(tradesService.processNewOrder(any(), any())).thenReturn(true);

        firstOrder = new OrderPairDTO();
        firstOrder.setId(1l);
        firstOrder.setPairId(pair.getId());
        firstOrder.setStatus(OrderStatusType.NEW);
        firstOrder.setType(OrderType.BUY);
        firstOrder.setValue(new BigInteger("5"));
        firstOrder.setRate(new BigDecimal("1.1"));
        firstOrder.setUserId(1l);
        // firstOrder = orderPairService.save(firstOrder, false, "user");
        secondOrder = new OrderPairDTO();
        secondOrder.setId(2l);
        secondOrder.setPairId(pair.getId());
        secondOrder.setStatus(OrderStatusType.NEW);
        secondOrder.setType(OrderType.SELL);
        secondOrder.setValue(new BigInteger("5"));
        secondOrder.setRate(new BigDecimal("1.1"));
        secondOrder.setUserId(2l);

        Map<Long, KeysResponseDTO> keys = new HashMap<>();
        keys.put(firstOrder.getUserId(), new KeysResponseDTO());
        keys.put(secondOrder.getUserId(), new KeysResponseDTO());
        when(userService.getKeys(any())).thenReturn(keys);
    }


    @Test
    public void testOrderExecution() throws InterruptedException {
        OrderPairDTO rs = orderPairService.save(firstOrder, false, 1l);
        verify(orderPairFeeService, times(1)).withdrawOrderFee(any(), any(), any());
        verify(tradesService, times(1)).processNewOrder(any(), any());
        assertThat(rs).isNotNull();
    }

    @Test
    public void testOrderDirectExecution() throws InterruptedException {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        secondOrder = orderPairService.save(firstOrder, true, 2l);

        verify(orderPairFeeService, times(2)).withdrawOrderFee(any(), any(), any());
        verify(tradesService, times(2)).processNewOrder(any(), any());
        assertThat(firstOrder.getCreateDate()).isNotNull();
        assertThat(secondOrder.getCreateDate()).isNotNull();

    }

    @Test(expected = UserNotFound.class)
    public void testUserNotFound() {
        CurrencyPair pair = currencyPairRepository.findByBuyCurrencyNameAndSellCurrencyName(buy, sell);

        OrderPairDTO orderPairDTO = new OrderPairDTO();
        orderPairDTO.setPairId(pair.getId());
        orderPairDTO.setValue(new BigInteger("1"));
        orderPairDTO.setRate(new BigDecimal("2.2"));
        orderPairDTO.setType(OrderType.BUY);

        orderPairService.save(orderPairDTO, false, null);
    }

    @Test
    public void testBuyOrderExecution() throws InterruptedException {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        secondOrder = orderPairService.save(secondOrder, false, 2l);

        verify(orderPairFeeService, times(2)).withdrawOrderFee(any(), any(), any());
        verify(tradesService, times(2)).processNewOrder(any(), any());
        assertThat(firstOrder.getCreateDate()).isNotNull();
        assertThat(secondOrder.getCreateDate()).isNotNull();

        assertThat(orderPairService.findOne(firstOrder.getId(), firstOrder.getPairId(), OrderStatusType.OPEN)).isNotNull();
        assertThat(orderPairService.findAll(Pageable.unpaged(), OrderStatusType.OPEN, firstOrder.getPairId()).getTotalElements())
            .isEqualTo(2);


    }

    @Test(expected = OrderPairExecutionException.class)
    public void testMigrateEmpty() {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        orderPairService.migrate(firstOrder.getId(), firstOrder.getPairId(), firstOrder.getStatus());

    }

    @Test
    public void testMigrateFull() {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        secondOrder = orderPairService.save(firstOrder, true, 2l);
        orderPairService.addExecution(firstOrder.getId(), pair.getId(), secondOrder.getId());
        Map<Long, Long> res = orderPairService.migrate(firstOrder.getId(), firstOrder.getPairId(), OrderStatusType.EXECUTED);
        assertThat(res.size()).isEqualTo(2);
        assertThat(orderPairService.findOne(res.get(firstOrder.getId()), firstOrder.getPairId(), OrderStatusType.OPEN)).isNull();
        assertThat(orderPairService.findOne(res.get(secondOrder.getId()), secondOrder.getPairId(), OrderStatusType.EXECUTED)).isNotNull();

    }

    @Test(expected = OrderPairCancelException.class)
    public void testCancelWrongStatus() {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        secondOrder = orderPairService.save(secondOrder, false, 2l);
        orderPairService.addExecution(firstOrder.getId(), pair.getId(), secondOrder.getId());
        orderPairService.cancel(firstOrder.getId(), pair.getId(), "0x");

    }

    @Test(expected = OrderPairCancelException.class)
    public void testCancelWrongUser() {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        secondOrder = orderPairService.save(secondOrder, false, 2l);
        orderPairService.addExecution(firstOrder.getId(), pair.getId(), secondOrder.getId());
        orderPairService.cancel(firstOrder.getId(), pair.getId(), "0x");

    }


    @Test
    public void testCancelOk() {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        orderPairService.setStatusOpen(firstOrder.getId(), pair.getId());
        orderPairService.cancel(firstOrder.getId(), pair.getId(), "0x");
        assertThat(orderPairService.findAll(Pageable.unpaged(), OrderStatusType.CANCELLED, firstOrder.getPairId()).getTotalElements())
            .isEqualTo(1);
    }

    @Test
    public void testDecouple() {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        secondOrder = orderPairService.save(firstOrder, true, 2l);
        orderPairService.addExecution(firstOrder.getId(), pair.getId(), secondOrder.getId());

        assertThat(orderPairService.findOne(firstOrder.getId(), firstOrder.getPairId(), OrderStatusType.OPEN).getExecutions().size())
            .isEqualTo(1);

        Long newId = orderPairService.decouplePartialOrder(firstOrder.getId(), secondOrder.getId(), pair.getId(), new BigInteger("1"));

        assertThat(orderPairService.findOne(firstOrder.getId(), firstOrder.getPairId(), OrderStatusType.OPEN).getExecutions().size())
            .isEqualTo(1);
        assertThat(orderPairService.findOne(newId, firstOrder.getPairId(), OrderStatusType.OPEN).getExecutions().size())
            .isEqualTo(0);

    }

    @Test
    public void testCancelOrders() throws InterruptedException {
        firstOrder = orderPairService.save(firstOrder, false, 1l);
        orderPairService.setStatusOpen(firstOrder.getId(),pair.getId());
        orderPairService.cancelOrders(new HashSet<>(Arrays.asList(firstOrder.getId())), pair.getId(), userService.getKeys(new KeysRequestDTO()));

        verify(tradesService, times(1)).processNewOrder(any(), any());
    }


}
