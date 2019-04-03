package com.exc.service;

import com.exc.domain.TradeCalculator;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class TradeCalculatorTest {
    TradeCalculator tradeCalculator;

    @Before
    public void setup() {
        tradeCalculator = new TradeCalculator(new BigInteger("5"));
        assertThat(tradeCalculator.isExExtra()).isFalse();

    }

    @Test
    public void checkDiffTotalEq() {
        tradeCalculator.calcTrade(new BigInteger("5"));
        assertThat(tradeCalculator.getDiff()).isEqualTo(new BigInteger("5"));

    }

    @Test
    public void checkDiffTotalLess() {
        tradeCalculator.calcTrade(new BigInteger("1"));
        assertThat(tradeCalculator.getDiff()).isEqualTo(BigInteger.ONE);
        assertThat(tradeCalculator.isFirst()).isFalse();
    }

    @Test
    public void checkDiffTotalMoreFirst() {
        tradeCalculator.calcTrade(new BigInteger("8"));
        assertThat(tradeCalculator.getDiff()).isEqualTo(new BigInteger("5"));
        assertThat(tradeCalculator.isFirst()).isTrue();
    }

    @Test
    public void checkMultipleRounds() {
        tradeCalculator.calcTrade(new BigInteger("1"));
        tradeCalculator.calcTrade(new BigInteger("2"));
        assertThat(tradeCalculator.getDiff()).isEqualTo(new BigInteger("2"));
        tradeCalculator.calcTrade(new BigInteger("5"));
        assertThat(tradeCalculator.getDiff()).isEqualTo(new BigInteger("2"));
        assertThat(tradeCalculator.isExExtra()).isTrue();

    }
}
