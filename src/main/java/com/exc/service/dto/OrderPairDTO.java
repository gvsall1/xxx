package com.exc.service.dto;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.enumeration.OrderStatusType;

/**
 * A DTO for the OrderPair entity.
 */
public class OrderPairDTO implements Serializable {

    private Long id;

    private ZonedDateTime createDate;

    private ZonedDateTime cancelDate;

    private ZonedDateTime modifyDate;

    private ZonedDateTime executedDate;

    private BigInteger value;

    private BigDecimal rate;

    private OrderType type;

    private OrderStatusType status;

    private Long pairId;

    private Set<OrderPairDTO> executions = new HashSet<>();

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(ZonedDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ZonedDateTime getExecutedDate() {
        return executedDate;
    }

    public void setExecutedDate(ZonedDateTime executedDate) {
        this.executedDate = executedDate;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatusType getStatus() {
        return status;
    }

    public void setStatus(OrderStatusType status) {
        this.status = status;
    }

    public Long getPairId() {
        return pairId;
    }

    public void setPairId(Long currencyPairId) {
        this.pairId = currencyPairId;
    }

    public Set<OrderPairDTO> getExecutions() {
        return executions;
    }

    public void setExecutions(Set<OrderPairDTO> orderPairs) {
        this.executions = orderPairs;
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

        OrderPairDTO orderPairDTO = (OrderPairDTO) o;
        if (orderPairDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderPairDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderPairDTO{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", cancelDate='" + getCancelDate() + "'" +
            ", modifyDate='" + getModifyDate() + "'" +
            ", executedDate='" + getExecutedDate() + "'" +
            ", value=" + getValue() +
            ", rate=" + getRate() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", pair=" + getPairId() +
            ", userId=" + getUserId() +
            "}";
    }
}
