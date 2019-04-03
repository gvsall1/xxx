package com.exc.service.dto;

import com.exc.domain.CurrencyName;

import java.io.Serializable;
import java.math.BigInteger;

public class UserWalletResponseDTO implements Serializable {


    public CurrencyName curIdBuy;
    public String privateBuyKey;
    public String privateSellKey;
    public long externalIdBuy;
    public CurrencyName curIdSell;

    public long externalIdSell;
}
