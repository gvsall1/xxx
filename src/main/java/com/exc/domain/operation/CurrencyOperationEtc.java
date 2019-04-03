package com.exc.domain.operation;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * User currency operation type
 */
@ApiModel(description = "User currency operation type")
@Entity
@Table(name = "co_etc")

public class CurrencyOperationEtc extends CurrencyOperation {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "co_etc_seq")
    @SequenceGenerator(name = "co_etc_seq", sequenceName = "co_etc_seq", allocationSize = 1)
    private Long Id;

    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public void setId(Long id) {
        Id = id;
    }

}
