package com.exc.service.mapper.order;

import com.exc.domain.order.OrderPairEthBtcOther;
import com.exc.service.mapper.CurrencyPairMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CurrencyPairMapper.class})
public interface OrderPairEthBtcOtherMapper extends OrderPairEntityMapper<OrderPairEthBtcOther> {
    @Override
    default OrderPairEthBtcOther fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderPairEthBtcOther orderPair = new OrderPairEthBtcOther();
        orderPair.setId(id);
        return orderPair;
    }
}
