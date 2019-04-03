package com.exc.service.mapper.order;

import com.exc.domain.order.OrderPairEtcBtcOther;
import com.exc.service.mapper.CurrencyPairMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CurrencyPairMapper.class})
public interface OrderPairEtcBtcOtherMapper extends OrderPairEntityMapper<OrderPairEtcBtcOther> {
    @Override
    default OrderPairEtcBtcOther fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderPairEtcBtcOther orderPair = new OrderPairEtcBtcOther();
        orderPair.setId(id);
        return orderPair;
    }
}
