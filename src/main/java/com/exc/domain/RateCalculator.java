package com.exc.domain;

import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.service.errors.OrderStatusException;
import com.exc.service.errors.PairDoesNotFitException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * convert exchange rates
 */
public class RateCalculator {
    OrderPair fromOrder, toOrder, buyOrder, sellOrder;
    BigInteger value;
    boolean reverseRate;

    public RateCalculator(OrderPair fromOrder, OrderPair toOrder, BigInteger value) {
        if (!fromOrder.getPair().equals(toOrder.getPair())) {
            throw new PairDoesNotFitException(String.format("Tried to execute orders with different currency pairs. Order1 %d Order2 %d pairId1 %d pairId2 %d", fromOrder.getId(), toOrder.getId(), fromOrder.getPair().getId(), toOrder.getPair().getId()));
        }
        this.fromOrder = fromOrder;
        this.toOrder = toOrder;
        this.value = value;

        if (fromOrder.getType().equals(OrderType.BUY) && toOrder.getType().equals(OrderType.SELL)) {
            buyOrder = fromOrder;
            sellOrder = toOrder;
        } else if (fromOrder.getType().equals(OrderType.SELL) && toOrder.getType().equals(OrderType.BUY)) {
            buyOrder = toOrder;
            sellOrder = fromOrder;
            reverseRate = true;
        } else {
            throw new OrderStatusException(String.format("Order status types must be different, order1 %d, order2 %d pairId %d ", fromOrder.getId(), toOrder.getId(), toOrder.getPair().getId()));
        }
    }

    public strictfp BigInteger getSellValue() {
        return value;

    }

    public strictfp BigInteger getBuyValue() {
        return (new BigDecimal(value).divide(buyOrder.getRate(), 50, RoundingMode.HALF_DOWN)).toBigInteger();
    }

    public strictfp BigInteger convertSellBalance(BigInteger sellBalance) {
        return new BigDecimal(sellBalance).divide(getSellOrder().getRate(), 50, RoundingMode.HALF_DOWN).toBigInteger();
    }

    public OrderPair getFromOrder() {
        return fromOrder;
    }

    public void setFromOrder(OrderPair fromOrder) {
        this.fromOrder = fromOrder;
    }

    public OrderPair getToOrder() {
        return toOrder;
    }

    public void setToOrder(OrderPair toOrder) {
        this.toOrder = toOrder;
    }

    public OrderPair getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(OrderPair buyOrder) {
        this.buyOrder = buyOrder;
    }

    public OrderPair getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(OrderPair sellOrder) {
        this.sellOrder = sellOrder;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public boolean isReverseRate() {
        return reverseRate;
    }

    public void setReverseRate(boolean reverseRate) {
        this.reverseRate = reverseRate;
    }
}
