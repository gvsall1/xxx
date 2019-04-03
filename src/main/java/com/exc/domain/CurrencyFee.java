package com.exc.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * currency fee settings
 */
@ApiModel(description = "currency fee settings")
@Entity
@Table(name = "currency_fee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CurrencyFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * fee during deposit operation
     */
    @ApiModelProperty(value = "fee during deposit operation")
    @Column(name = "deposit", precision = 10, scale = 2)
    private BigInteger deposit;

    /**
     * fee during withdraw operation
     */
    @ApiModelProperty(value = "fee during withdraw operation")
    @Column(name = "withdraw", precision = 10, scale = 2)
    private BigInteger withdraw;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getDeposit() {
        return deposit;
    }

    public CurrencyFee deposit(BigInteger deposit) {
        this.deposit = deposit;
        return this;
    }

    public void setDeposit(BigInteger deposit) {
        this.deposit = deposit;
    }

    public BigInteger getWithdraw() {
        return withdraw;
    }

    public CurrencyFee withdraw(BigInteger withdraw) {
        this.withdraw = withdraw;
        return this;
    }

    public void setWithdraw(BigInteger withdraw) {
        this.withdraw = withdraw;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrencyFee currencyFee = (CurrencyFee) o;
        if (currencyFee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyFee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyFee{" +
            "id=" + getId() +
            ", deposit=" + getDeposit() +
            ", withdraw=" + getWithdraw() +
            "}";
    }
}
