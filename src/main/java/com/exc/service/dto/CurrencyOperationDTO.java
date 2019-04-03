package com.exc.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

import com.exc.domain.CurrencyName;
import com.exc.domain.enumeration.OperationType;

/**
 * A DTO for the CurrencyOperation entity.
 */
public class CurrencyOperationDTO implements Serializable {

    private Long id;

    private OperationType type;

    private ZonedDateTime createDate;

    private ZonedDateTime validateDate;

    private String message;

    private Boolean isValid;

    private Long tx;

    private String receiverAddress;

    private BigInteger value;

    private Long currencyId;

    private CurrencyName currencyName;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(ZonedDateTime validateDate) {
        this.validateDate = validateDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Long getTx() {
        return tx;
    }

    public void setTx(Long tx) {
        this.tx = tx;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long cryptoCurrencyId) {
        this.currencyId = cryptoCurrencyId;
    }

    public CurrencyName getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(CurrencyName cryptoCurrencyName) {
        this.currencyName = cryptoCurrencyName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyOperationDTO currencyOperationDTO = (CurrencyOperationDTO) o;
        if (currencyOperationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyOperationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyOperationDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", validateDate='" + getValidateDate() + "'" +
            ", message='" + getMessage() + "'" +
            ", isValid='" + isIsValid() + "'" +
            ", txId='" + getTx() + "'" +
            ", receiverAddress='" + getReceiverAddress() + "'" +
            ", value=" + getValue() +
            ", currency=" + getCurrencyId() +
            ", currency='" + getCurrencyName() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
