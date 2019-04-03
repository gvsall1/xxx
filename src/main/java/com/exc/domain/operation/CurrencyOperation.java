package com.exc.domain.operation;

import com.exc.domain.CryptoCurrency;
import com.exc.domain.enumeration.OperationType;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * User currency operation type
 *  There is table per Operation.
 */
@MappedSuperclass
public abstract class CurrencyOperation implements Serializable {

    private static final long serialVersionUID = 1L;

   /* @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;*/

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private OperationType type;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "validate_date")
    private ZonedDateTime validateDate;

    /**
     * operation details that informs user about this operation
     */
    @ApiModelProperty(value = "operation details that informs user about this operation")
    @Column(name = "message")
    private String message;

    /**
     * indicate if this operation were success
     */
    @ApiModelProperty(value = "indicate if this operation were success")
    @Column(name = "is_valid")
    private Boolean isValid;

    /**
     * transaction ID
     */
    @ApiModelProperty(value = "transaction ID")
    @Column(name = "jhi_tx")
    private Long tx;

    /**
     * receiver public key
     */
    @ApiModelProperty(value = "receiver public key")
    @Column(name = "receiver_address")
    private String receiverAddress;

    @Column(name = "jhi_value")
    private BigInteger value;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    private CryptoCurrency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public abstract Long getId();

    public abstract void setId(Long id);

    public OperationType getType() {
        return type;
    }

    public CurrencyOperation type(OperationType type) {
        this.type = type;
        return this;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public CurrencyOperation createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getValidateDate() {
        return validateDate;
    }

    public CurrencyOperation validateDate(ZonedDateTime validateDate) {
        this.validateDate = validateDate;
        return this;
    }

    public void setValidateDate(ZonedDateTime validateDate) {
        this.validateDate = validateDate;
    }

    public String getMessage() {
        return message;
    }

    public CurrencyOperation message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isIsValid() {
        return isValid;
    }

    public CurrencyOperation isValid(Boolean isValid) {
        this.isValid = isValid;
        return this;
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

    public CurrencyOperation receiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
        return this;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public BigInteger getValue() {
        return value;
    }

    public CurrencyOperation value(BigInteger value) {
        this.value = value;
        return this;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public CurrencyOperation currency(CryptoCurrency cryptoCurrency) {
        this.currency = cryptoCurrency;
        return this;
    }

    public void setCurrency(CryptoCurrency cryptoCurrency) {
        this.currency = cryptoCurrency;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        CurrencyOperation currencyOperation = (CurrencyOperation) o;
        if (currencyOperation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyOperation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyOperation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", validateDate='" + getValidateDate() + "'" +
            ", message='" + getMessage() + "'" +
            ", isValid='" + isIsValid() + "'" +
            ", tx='" + getTx() + "'" +
            ", receiverAddress='" + getReceiverAddress() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
