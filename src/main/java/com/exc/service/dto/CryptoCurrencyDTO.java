package com.exc.service.dto;

import com.exc.domain.CurrencyName;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the CryptoCurrency entity.
 */
public class CryptoCurrencyDTO implements Serializable {

    private Long id;

    @NotNull
    private CurrencyName currencyName;

    private String rateUrl;

    private Boolean isToken;

    private String nodeUrlSendTX;

    private String nodeStatus;

    private BigDecimal minAmount;

    private Long feeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyName getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(CurrencyName currencyName) {
        this.currencyName = currencyName;
    }

    public String getRateUrl() {
        return rateUrl;
    }

    public void setRateUrl(String rateUrl) {
        this.rateUrl = rateUrl;
    }

    public Boolean isIsToken() {
        return isToken;
    }

    public void setIsToken(Boolean isToken) {
        this.isToken = isToken;
    }

    public String getNodeUrlSendTX() {
        return nodeUrlSendTX;
    }

    public void setNodeUrlSendTX(String nodeUrlSendTX) {
        this.nodeUrlSendTX = nodeUrlSendTX;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long currencyFeeId) {
        this.feeId = currencyFeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CryptoCurrencyDTO cryptoCurrencyDTO = (CryptoCurrencyDTO) o;
        if (cryptoCurrencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cryptoCurrencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CryptoCurrencyDTO{" +
            "id=" + getId() +
            ", name='" + getCurrencyName() + "'" +
            ", rateUrl='" + getRateUrl() + "'" +
            ", isToken='" + isIsToken() + "'" +
            ", nodeUrlSendTX='" + getNodeUrlSendTX() + "'" +
            ", nodeStatus='" + getNodeStatus() + "'" +
            ", minAmount=" + getMinAmount() +
            ", fee=" + getFeeId() +
            "}";
    }
}
