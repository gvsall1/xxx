package com.exc.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the OrderPairFee entity.
 */
public class OrderPairFeeDTO implements Serializable {

    private Long id;

    private BigDecimal placeOrder;

    private BigDecimal modifyOrder;

    private BigDecimal deleteOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(BigDecimal placeOrder) {
        this.placeOrder = placeOrder;
    }

    public BigDecimal getModifyOrder() {
        return modifyOrder;
    }

    public void setModifyOrder(BigDecimal modifyOrder) {
        this.modifyOrder = modifyOrder;
    }

    public BigDecimal getDeleteOrder() {
        return deleteOrder;
    }

    public void setDeleteOrder(BigDecimal deleteOrder) {
        this.deleteOrder = deleteOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderPairFeeDTO orderPairFeeDTO = (OrderPairFeeDTO) o;
        if (orderPairFeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderPairFeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderPairFeeDTO{" +
            "id=" + getId() +
            ", placeOrder=" + getPlaceOrder() +
            ", modifyOrder=" + getModifyOrder() +
            ", deleteOrder=" + getDeleteOrder() +
            "}";
    }
}
