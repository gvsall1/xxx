package com.exc.service;

import com.exc.domain.CryptoCurrency;
import com.exc.domain.CurrencyName;
import com.exc.domain.CurrencyPair;
import com.exc.domain.OrderPairFee;
import com.exc.domain.enumeration.CryptoCurrencyTransactionType;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.domain.order.OrderPairEthBtcOther;
import com.exc.repository.CurrencyPairRepository;
import com.exc.service.remote.TxService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class OrderPairFeeServiceTest extends AbstractServiceTest {

    @Autowired
    OrderPairFeeService orderPairFeeService;
    @Autowired
    CurrencyPairRepository currencyPairRepository;

    @Before
    public void mock() {
        when(txService.withdrawOrderFee(any())).thenReturn(1l);
    }


    @Test
    public void shouldCheckFeeWithdraw() {

        CurrencyPair pair = new CurrencyPair();
        pair.setBuy(new CryptoCurrency());
        pair.setSell(new CryptoCurrency());
        pair.getBuy().setCurrencyName(CurrencyName.ETH);
        pair.getSell().setCurrencyName(CurrencyName.BTC);
        pair.setFee(new OrderPairFee());
        pair.getFee().setPlaceOrder(BigInteger.ONE);
        assertThat(pair).isNotNull();


        OrderPair orderPair = new OrderPairEthBtcOther();
        orderPair.setPair(pair);
        orderPair.setId(100500l);
        orderPair.setUserId(1l);
        orderPair.setType(OrderType.BUY);
        Long tx = orderPairFeeService.withdrawOrderFee(orderPair, OrderStatusType.NEW, "0x1");

        assertThat(tx).isNotNull();

    }
}
