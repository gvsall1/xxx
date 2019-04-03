package com.exc.service.mapper.order;

import com.exc.domain.order.OrderPairEthBtcOpen;
import com.exc.service.mapper.CurrencyPairMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CurrencyPairMapper.class})
public interface OrderPairEthBtcOpenMapper extends OrderPairEntityMapper<OrderPairEthBtcOpen> {
    @Override
    default OrderPairEthBtcOpen fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderPairEthBtcOpen orderPair = new OrderPairEthBtcOpen();
        orderPair.setId(id);
        return orderPair;
    }
}
