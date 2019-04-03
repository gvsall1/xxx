package com.exc.service.dto;

import com.exc.domain.CurrencyName;
import com.exc.domain.RateCalculator;

import java.io.Serializable;
import java.math.BigInteger;

public class UserWalletRequestDTO implements Serializable {
    private CurrencyName currencyName;
    private Long buyerId;
    private Long sellerId;

    public UserWalletRequestDTO(CurrencyName currencyName, Long buyerId, Long sellerId) {
        this.currencyName = currencyName;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }

    public CurrencyName getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(CurrencyName currencyName) {
        this.currencyName = currencyName;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}
