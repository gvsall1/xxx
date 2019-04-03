package com.exc.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CurrencyPair entity.
 */
public class CurrencyPairDTO implements Serializable {

    private Long id;

    private Long buyId;

    private String buyName;

    private Long sellId;

    private String sellName;

    private Long feeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyId() {
        return buyId;
    }

    public void setBuyId(Long cryptoCurrencyId) {
        this.buyId = cryptoCurrencyId;
    }

    public String getBuyName() {
        return buyName;
    }

    public void setBuyName(String cryptoCurrencyName) {
        this.buyName = cryptoCurrencyName;
    }

    public Long getSellId() {
        return sellId;
    }

    public void setSellId(Long cryptoCurrencyId) {
        this.sellId = cryptoCurrencyId;
    }

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String cryptoCurrencyName) {
        this.sellName = cryptoCurrencyName;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long orderPairFeeId) {
        this.feeId = orderPairFeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyPairDTO currencyPairDTO = (CurrencyPairDTO) o;
        if (currencyPairDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyPairDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyPairDTO{" +
            "id=" + getId() +
            ", buy=" + getBuyId() +
            ", buy='" + getBuyName() + "'" +
            ", sell=" + getSellId() +
            ", sell='" + getSellName() + "'" +
            ", fee=" + getFeeId() +
            "}";
    }
}
