package com.exc.service.mapper;

import com.exc.domain.*;
import com.exc.service.dto.OrderPairFeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrderPairFee and its DTO OrderPairFeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderPairFeeMapper extends EntityMapper<OrderPairFeeDTO, OrderPairFee> {



    default OrderPairFee fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderPairFee orderPairFee = new OrderPairFee();
        orderPairFee.setId(id);
        return orderPairFee;
    }
}
