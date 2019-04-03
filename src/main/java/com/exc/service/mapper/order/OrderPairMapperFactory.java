package com.exc.service.mapper.order;

import com.exc.domain.CurrencyName;
import com.exc.domain.enumeration.OrderStatusType;
import org.springframework.stereotype.Component;

@Component
public class OrderPairMapperFactory {

    private final OrderPairEtcBtcOpenMapper orderPairEtcBtcOpenMapper;
    private final OrderPairEtcBtcOtherMapper orderPairEtcBtcOtherMapper;
    private final OrderPairEthBtcOpenMapper orderPairEthBtcOpenMapper;
    private final OrderPairEthBtcOtherMapper orderPairEthBtcOtherMapper;

    public OrderPairMapperFactory(OrderPairEtcBtcOpenMapper orderPairEtcBtcOpenMapper, OrderPairEtcBtcOtherMapper orderPairEtcBtcOtherMapper, OrderPairEthBtcOpenMapper orderPairEthBtcOpenMapper, OrderPairEthBtcOtherMapper orderPairEthBtcOtherMapper) {
        this.orderPairEtcBtcOpenMapper = orderPairEtcBtcOpenMapper;
        this.orderPairEtcBtcOtherMapper = orderPairEtcBtcOtherMapper;
        this.orderPairEthBtcOpenMapper = orderPairEthBtcOpenMapper;
        this.orderPairEthBtcOtherMapper = orderPairEthBtcOtherMapper;
    }

    public OrderPairEntityMapper getMapper(CurrencyName buy, CurrencyName sell, OrderStatusType statusType) {
        OrderPairEntityMapper res = null;
        boolean isOpen = statusType.equals(OrderStatusType.OPEN) || statusType.equals(OrderStatusType.IN_PROCESS) || statusType.equals(OrderStatusType.NEW);
        String key = buy.name() + "-" + sell.name();
        switch (key.toLowerCase()) {
            case "eth-btc":
                res = isOpen ? orderPairEthBtcOpenMapper : orderPairEthBtcOtherMapper;
                break;
            case "etc-btc":
                res = isOpen ? orderPairEtcBtcOpenMapper : orderPairEtcBtcOtherMapper;
                break;
        }
        return res;
    }
}
