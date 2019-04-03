package com.exc.domain.order;

import com.exc.domain.CurrencyPair;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Order created by trader.
 * There are two tables per currency. Each currency has active(trade) and not active orders(history)
 *
 * @param <T>
 */
@MappedSuperclass
public abstract class OrderPair<T extends OrderPair> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "cancel_date")
    private ZonedDateTime cancelDate;

    @Column(name = "modify_date")
    private ZonedDateTime modifyDate;

    @Column(name = "executed_date")
    private ZonedDateTime executedDate;

    @Column(name = "jhi_value")
    private BigInteger value;

    //todo add @OrderColumn
    @Column(name = "rate")
    private BigDecimal rate;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private OrderType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatusType status;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private CurrencyPair pair;

    @OneToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<T> executions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public abstract Long getId();

    public abstract void setId(Long id);

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public OrderPair createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getCancelDate() {
        return cancelDate;
    }

    public OrderPair cancelDate(ZonedDateTime cancelDate) {
        this.cancelDate = cancelDate;
        return this;
    }

    public void setCancelDate(ZonedDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public OrderPair modifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ZonedDateTime getExecutedDate() {
        return executedDate;
    }

    public OrderPair executedDate(ZonedDateTime executedDate) {
        this.executedDate = executedDate;
        return this;
    }

    public void setExecutedDate(ZonedDateTime executedDate) {
        this.executedDate = executedDate;
    }

    public BigInteger getValue() {
        return value;
    }

    public OrderPair value(BigInteger value) {
        this.value = value;
        return this;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public OrderPair rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public OrderType getType() {
        return type;
    }

    public OrderPair type(OrderType type) {
        this.type = type;
        return this;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatusType getStatus() {
        return status;
    }

    public OrderPair status(OrderStatusType status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatusType status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

 /*

    public Set<Tx> getTxes() {
        if (txes == null) {
            txes = new HashSet<>();
        }
        return txes;
    }

    public OrderPair txes(Set<Tx> cryptoCurrencyTransactions) {
        this.txes = cryptoCurrencyTransactions;
        return this;
    }

    public OrderPair addTx(Tx cryptoCurrencyTransaction) {
        getTxes().add(cryptoCurrencyTransaction);
      //  cryptoCurrencyTransaction.setOrderPair(this);
        return this;
    }

    public OrderPair removeTx(CryptoCurrencyTransaction cryptoCurrencyTransaction) {
        getTxes().remove(cryptoCurrencyTransaction);
      //  cryptoCurrencyTransaction.setOrderPair(null);
        return this;
    }

    public void setTxes(Set<Tx> cryptoCurrencyTransactions) {
        this.txes = cryptoCurrencyTransactions;
    }
*/

    public CurrencyPair getPair() {
        return pair;
    }

    public OrderPair pair(CurrencyPair currencyPair) {
        this.pair = currencyPair;
        return this;
    }

    public void setPair(CurrencyPair currencyPair) {
        this.pair = currencyPair;
    }


    public Set<T> getExecutions() {
        return new HashSet<>(executions);
    }

    public int sizeOfExecutions() {
        return executions.size();
    }

    public OrderPair executions(Set<T> orderPairs) {
        this.executions = orderPairs;
        return this;
    }

    public OrderPair addExecution(T orderPair) {
        this.executions.add(orderPair);
        return this;
    }

    public OrderPair removeExecution(OrderPair orderPair) {
        this.executions.remove(orderPair);
        return this;
    }

    public void setExecutions(Set<T> orderPairs) {
        this.executions = orderPairs;
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
        OrderPair orderPair = (OrderPair) o;
        if (orderPair.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderPair.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderPair{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            ", executedDate='" + getExecutedDate() + "'" +
            ", value=" + getValue() +
            ", rate=" + getRate() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
