package com.exc.service.mapper.order;

import com.exc.domain.order.OrderPairEtcBtcOpen;
import com.exc.service.mapper.CurrencyPairMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CurrencyPairMapper.class})
public interface OrderPairEtcBtcOpenMapper extends OrderPairEntityMapper<OrderPairEtcBtcOpen> {
    @Override
    default OrderPairEtcBtcOpen fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderPairEtcBtcOpen orderPair = new OrderPairEtcBtcOpen();
        orderPair.setId(id);
        return orderPair;
    }
}
