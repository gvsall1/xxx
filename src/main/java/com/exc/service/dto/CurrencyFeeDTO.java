package com.exc.service.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A DTO for the CurrencyFee entity.
 */
public class CurrencyFeeDTO implements Serializable {

    private Long id;

    private BigInteger deposit;

    private BigInteger withdraw;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getDeposit() {
        return deposit;
    }

    public void setDeposit(BigInteger deposit) {
        this.deposit = deposit;
    }

    public BigInteger getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(BigInteger withdraw) {
        this.withdraw = withdraw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyFeeDTO currencyFeeDTO = (CurrencyFeeDTO) o;
        if (currencyFeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyFeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyFeeDTO{" +
            "id=" + getId() +
            ", deposit=" + getDeposit() +
            ", withdraw=" + getWithdraw() +
            "}";
    }
}
