package com.exc.domain.order;


import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "order_pair_eth_btc_other")
@ApiModel(description = "btc - eth other order type implementation")
@AssociationOverride(name = "executions", joinTable = @JoinTable(name = "order_pair_eth_btc_other_exec",
    joinColumns = @JoinColumn(name = "execution_of_id", referencedColumnName = "id", insertable = false, updatable = false),
    inverseJoinColumns = @JoinColumn(name = "executions_id", referencedColumnName = "id", insertable = false, updatable = false)))

public class OrderPairEthBtcOther extends OrderPair<OrderPairEthBtcOther> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "op_eth_btc_seq")
    @SequenceGenerator(name = "op_eth_btc_seq", sequenceName = "op_eth_btc_seq", allocationSize = 1)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
