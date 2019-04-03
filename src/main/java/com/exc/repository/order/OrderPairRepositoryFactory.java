package com.exc.repository.order;

import com.exc.domain.CurrencyName;
import com.exc.domain.enumeration.OrderStatusType;
import org.springframework.stereotype.Component;

@Component
public class OrderPairRepositoryFactory {
    private final OrderPairEtcBtcOpenRepository orderPairEtcBtcOpenRepository;
    private final OrderPairEtcBtcOtherRepository orderPairEtcBtcOtherRepository;
    private final OrderPairEthBtcOpenRepository orderPairEthBtcOpenRepository;
    private final OrderPairEthBtcOtherRepository orderPairEthBtcOtherRepository;

    public OrderPairRepositoryFactory(OrderPairEtcBtcOpenRepository orderPairEtcBtcOpenRepository, OrderPairEtcBtcOtherRepository orderPairEtcBtcOtherRepository, OrderPairEthBtcOpenRepository orderPairEthBtcOpenRepository, OrderPairEthBtcOtherRepository orderPairEthBtcOtherRepository) {
        this.orderPairEtcBtcOpenRepository = orderPairEtcBtcOpenRepository;
        this.orderPairEtcBtcOtherRepository = orderPairEtcBtcOtherRepository;
        this.orderPairEthBtcOpenRepository = orderPairEthBtcOpenRepository;
        this.orderPairEthBtcOtherRepository = orderPairEthBtcOtherRepository;
    }


    public OrderPairRepository getOrderPairRepo(CurrencyName buy, CurrencyName sell, OrderStatusType statusType) {
        OrderPairRepository res = null;
        boolean isOpen = statusType.equals(OrderStatusType.OPEN) || statusType.equals(OrderStatusType.IN_PROCESS) || statusType.equals(OrderStatusType.NEW);
        String key = buy.name() + "-" + sell.name();
        switch (key.toLowerCase()) {
            case "eth-btc":
                res = isOpen ? orderPairEthBtcOpenRepository : orderPairEthBtcOtherRepository;
                break;
            case "etc-btc":
                res = isOpen ? orderPairEtcBtcOpenRepository : orderPairEtcBtcOtherRepository;
                break;
        }
        return res;
    }
}
