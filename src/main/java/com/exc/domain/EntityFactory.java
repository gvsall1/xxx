package com.exc.domain;

import com.exc.domain.enumeration.CryptoCurrencyTransactionStatus;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.operation.CurrencyOperation;
import com.exc.domain.operation.CurrencyOperationBtc;
import com.exc.domain.operation.CurrencyOperationEtc;
import com.exc.domain.operation.CurrencyOperationEth;
import com.exc.domain.order.*;

import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * class exists to simplify entities creation
 */
@Component
public class EntityFactory {


    public CurrencyOperation makeOp(CurrencyName cur) {
        CurrencyOperation res = null;

        switch (cur) {
            case ETH:
                res = new CurrencyOperationEth();
                break;

            case ETC:
                res = new CurrencyOperationEtc();
                break;

            case BTC:
                res = new CurrencyOperationBtc();
                break;
        }
        return res;

    }

    /**
     * create new OrderPair   of required type
     *
     * @param buy
     * @param sell
     * @param statusType
     * @param orderPair  will clone fields if param not null
     * @return
     */
    public OrderPair makeOrder(CurrencyName buy, CurrencyName sell, OrderStatusType statusType, @Nullable OrderPair orderPair) {
        boolean isOpen = statusType.equals(OrderStatusType.OPEN) || statusType.equals(OrderStatusType.IN_PROCESS) || statusType.equals(OrderStatusType.NEW);
        String key = buy.name() + "-" + sell.name();
        OrderPair res = null;
        switch (key.toLowerCase()) {
            case "eth-btc":
                res = isOpen ? new OrderPairEthBtcOpen() : new OrderPairEthBtcOther();
                break;
            case "etc-btc":
                res = isOpen ? new OrderPairEtcBtcOpen() : new OrderPairEtcBtcOther();
                break;
        }
        res.setStatus(statusType);
        if (orderPair != null) {
            res.setUserId(orderPair.getUserId());
            res.setCreateDate(orderPair.getCreateDate());
            res.setType(orderPair.getType());
            res.setRate(orderPair.getRate());
            res.setValue(orderPair.getValue());
            res.setPair(orderPair.getPair());
           /* Set<OrderPair> executions = (Set<OrderPair>) orderPair.getExecutions().stream().map(ex -> makeOrder(buy, sell, statusType, (OrderPair) ex)).collect(Collectors.toSet());
            res.setExecutions(executions);*/
        }

        return res;
    }

}
