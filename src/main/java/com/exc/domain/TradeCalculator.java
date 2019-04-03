package com.exc.domain;

import java.math.BigInteger;

/**
 * calculate total trades
 */
public class TradeCalculator {
    private BigInteger toBuy;
    private BigInteger totalBought = new BigInteger("0");
    private BigInteger diff;
    // indicate that order value out of existing execution trades, and must be restarted
    private boolean exExtra;
    private int total;
    private boolean first;

    public TradeCalculator(BigInteger toBuy) {
        this.toBuy = toBuy;
    }

    public void calcTrade(BigInteger ex) {
        totalBought = totalBought.add(ex);
        total = totalBought.compareTo(toBuy);

        if (total <= 0) {
            diff = ex;
        } else if (total > 0) {
            totalBought = totalBought.subtract(ex);
            if (totalBought.compareTo(BigInteger.ZERO) == 0) {
                diff = toBuy;
                first = true;
            } else {
                exExtra = true;
                diff = toBuy.subtract(totalBought);
                totalBought = totalBought.add(diff);
            }
        }

    }

    public BigInteger getToBuy() {
        return toBuy;
    }

    public BigInteger getTotalBought() {
        return totalBought;
    }

    public BigInteger getDiff() {
        return diff;
    }

    public boolean isExExtra() {
        return exExtra;
    }

    public int getTotal() {
        return total;
    }

    public boolean isFirst() {
        return first;
    }
}
