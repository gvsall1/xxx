package com.exc.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A OrderPairFee.
 */
@Entity
@Table(name = "order_pair_fee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderPairFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * fee during placeOrder operation
     */
    @ApiModelProperty(value = "fee during placeOrder operation")
    @Column(name = "place_order", precision = 10, scale = 2)
    private BigInteger placeOrder;

    /**
     * fee during modifyOrder operation
     */
    @ApiModelProperty(value = "fee during modifyOrder operation")
    @Column(name = "modify_order", precision = 10, scale = 2)
    private BigInteger modifyOrder;

    /**
     * fee during deleteOrder operation
     */
    @ApiModelProperty(value = "fee during deleteOrder operation")
    @Column(name = "delete_order", precision = 10, scale = 2)
    private BigInteger deleteOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPlaceOrder() {
        return placeOrder;
    }

    public OrderPairFee placeOrder(BigInteger placeOrder) {
        this.placeOrder = placeOrder;
        return this;
    }

    public void setPlaceOrder(BigInteger placeOrder) {
        this.placeOrder = placeOrder;
    }

    public BigInteger getModifyOrder() {
        return modifyOrder;
    }

    public OrderPairFee modifyOrder(BigInteger modifyOrder) {
        this.modifyOrder = modifyOrder;
        return this;
    }

    public void setModifyOrder(BigInteger modifyOrder) {
        this.modifyOrder = modifyOrder;
    }

    public BigInteger getDeleteOrder() {
        return deleteOrder;
    }

    public OrderPairFee deleteOrder(BigInteger deleteOrder) {
        this.deleteOrder = deleteOrder;
        return this;
    }

    public void setDeleteOrder(BigInteger deleteOrder) {
        this.deleteOrder = deleteOrder;
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
        OrderPairFee orderPairFee = (OrderPairFee) o;
        if (orderPairFee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderPairFee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderPairFee{" +
            "id=" + getId() +
            ", placeOrder=" + getPlaceOrder() +
            ", modifyOrder=" + getModifyOrder() +
            ", deleteOrder=" + getDeleteOrder() +
            "}";
    }
}
