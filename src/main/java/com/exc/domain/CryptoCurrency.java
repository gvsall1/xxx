package com.exc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * currency presentation, including fiat
 */
@ApiModel(description = "currency presentation, including fiat")
@Entity
@Table(name = "crypto_currency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CryptoCurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * ETH,USD etc..
     */
    @NotNull
    @ApiModelProperty(value = "ETH,USD etc..", required = true)
    @Column(name = "currency_name")
    @Enumerated(EnumType.STRING)
    private CurrencyName currencyName;

    /**
     * Optional
     */
    @ApiModelProperty(value = "Optional")
    @Column(name = "rate_url")
    private String rateUrl;

    /**
     * indicate if this currency is token
     */
    @ApiModelProperty(value = "indicate if this currency is token")
    @Column(name = "is_token")
    private Boolean isToken;

    /**
     * Optional
     */
    @ApiModelProperty(value = "Optional")
    @Column(name = "node_url_send_tx")
    private String nodeUrlSendTX;

    /**
     * indicate currency node status
     */
    @ApiModelProperty(value = "indicate currency node status")
    @Column(name = "node_status")
    private String nodeStatus;

    /**
     * min money amount, to keep trading available
     */
    @ApiModelProperty(value = "min money amount, to keep trading available")
    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    /**
     * Currency fee details, during deposit/withdraw
     */
    @ApiModelProperty(value = "Currency fee details, during deposit/withdraw")
    @ManyToOne
    @JsonIgnoreProperties("")
    private CurrencyFee fee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public CryptoCurrency currencyName(CurrencyName currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public String getRateUrl() {
        return rateUrl;
    }

    public CryptoCurrency rateUrl(String rateUrl) {
        this.rateUrl = rateUrl;
        return this;
    }

    public void setRateUrl(String rateUrl) {
        this.rateUrl = rateUrl;
    }

    public Boolean isIsToken() {
        return isToken;
    }

    public CryptoCurrency isToken(Boolean isToken) {
        this.isToken = isToken;
        return this;
    }

    public void setIsToken(Boolean isToken) {
        this.isToken = isToken;
    }

    public String getNodeUrlSendTX() {
        return nodeUrlSendTX;
    }

    public CryptoCurrency nodeUrlSendTX(String nodeUrlSendTX) {
        this.nodeUrlSendTX = nodeUrlSendTX;
        return this;
    }

    public void setNodeUrlSendTX(String nodeUrlSendTX) {
        this.nodeUrlSendTX = nodeUrlSendTX;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public CryptoCurrency nodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
        return this;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public CryptoCurrency minAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
        return this;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public CurrencyFee getFee() {
        return fee;
    }

    public CryptoCurrency fee(CurrencyFee currencyFee) {
        this.fee = currencyFee;
        return this;
    }

    public void setFee(CurrencyFee currencyFee) {
        this.fee = currencyFee;
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
        CryptoCurrency cryptoCurrency = (CryptoCurrency) o;
        if (cryptoCurrency.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cryptoCurrency.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CryptoCurrency{" +
            "id=" + getId() +
            ", currencyName='" + getCurrencyName().name() + "'" +
            ", rateUrl='" + getRateUrl() + "'" +
            ", isToken='" + isIsToken() + "'" +
            ", nodeUrlSendTX='" + getNodeUrlSendTX() + "'" +
            ", nodeStatus='" + getNodeStatus() + "'" +
            ", minAmount=" + getMinAmount() +
            "}";
    }
}
