package com.exc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Part of business logic, it is has no relationship in DB layer, because of complexity/performance
 */
@ApiModel(description = "Part of business logic, it is has no relationship in DB layer, because of complexity/performance")
@Entity
@Table(name = "currency_pair")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CurrencyPair implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("")
    private CryptoCurrency buy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private CryptoCurrency sell;

    @ManyToOne
    @JsonIgnoreProperties("")
    private OrderPairFee fee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CryptoCurrency getBuy() {
        return buy;
    }

    public CurrencyPair buy(CryptoCurrency cryptoCurrency) {
        this.buy = cryptoCurrency;
        return this;
    }

    public void setBuy(CryptoCurrency cryptoCurrency) {
        this.buy = cryptoCurrency;
    }

    public CryptoCurrency getSell() {
        return sell;
    }

    public CurrencyPair sell(CryptoCurrency cryptoCurrency) {
        this.sell = cryptoCurrency;
        return this;
    }

    public void setSell(CryptoCurrency cryptoCurrency) {
        this.sell = cryptoCurrency;
    }

    public OrderPairFee getFee() {
        return fee;
    }

    public CurrencyPair fee(OrderPairFee orderPairFee) {
        this.fee = orderPairFee;
        return this;
    }

    public void setFee(OrderPairFee orderPairFee) {
        this.fee = orderPairFee;
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
        CurrencyPair currencyPair = (CurrencyPair) o;
        if (currencyPair.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyPair.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
            "id=" + getId() +
            "}";
    }
}
