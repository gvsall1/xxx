package com.exc.service;

import com.exc.domain.CurrencyPair;
import com.exc.domain.RateCalculator;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.domain.order.OrderPairEthBtcOpen;
import com.exc.service.errors.OrderStatusException;
import com.exc.service.errors.PairDoesNotFitException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class RateCalculatorTest {
    OrderPair buyOrder;
    OrderPair sellOrder;
    CurrencyPair pair;
    String buy = "eth", sell = "btc";
    RateCalculator rateCalculator;

    @Before
    public void resetOrders() {
        pair = new CurrencyPair();
        pair.setId(1l);

        if (buyOrder == null)
            buyOrder = new OrderPairEthBtcOpen();

        buyOrder.setId(1l);
        buyOrder.setPair(pair);
        buyOrder.setStatus(OrderStatusType.OPEN);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setValue(new BigInteger("100"));
        buyOrder.setRate(new BigDecimal("1.1"));
        if (sellOrder == null)
            sellOrder = new OrderPairEthBtcOpen();
        sellOrder.setId(2l);
        sellOrder.setPair(pair);
        sellOrder.setStatus(OrderStatusType.OPEN);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setValue(new BigInteger("100"));
        sellOrder.setRate(new BigDecimal("1.1"));
        buyOrder.addExecution(sellOrder);
    }

    @Test(expected = PairDoesNotFitException.class)
    public void checkRatePairExcp() {
        sellOrder.setPair(new CurrencyPair());

        rateCalculator = new RateCalculator(buyOrder, sellOrder, new BigInteger("1"));
    }
    @Test(expected = OrderStatusException.class)
    public void checkRateStatusExcp() {
        sellOrder.setType(OrderType.BUY);
        rateCalculator = new RateCalculator(buyOrder, sellOrder, new BigInteger("1"));
    }

    @Test
    public void checkRateEq() {
        rateCalculator = new RateCalculator(buyOrder, sellOrder, new BigInteger("100"));
        assertThat(rateCalculator.getBuyValue()).isEqualTo(new BigInteger("90"));
    }

    @Test
    public void checkRateMore() {
        buyOrder.setRate(new BigDecimal("27.56"));
        sellOrder.setRate(new BigDecimal("28"));
        rateCalculator = new RateCalculator(sellOrder,buyOrder , new BigInteger("100"));
        assertThat(rateCalculator.getBuyValue()).isEqualTo(new BigInteger("3"));
        assertThat(rateCalculator.getSellValue()).isEqualTo(new BigInteger("100"));
    }
}
