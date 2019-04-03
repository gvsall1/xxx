package com.exc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * helper class that run  orders/tx(entity) migration inside 1 spring transaction
 */
@Service
@Transactional
public class MigrationService {
    private final Logger log = LoggerFactory.getLogger(MigrationService.class);
    private OrderPairService orderPairService;
    private CurrencyPairService currencyPairService;

    public MigrationService(OrderPairService orderPairService, CurrencyPairService currencyPairService) {
        this.orderPairService = orderPairService;
        this.currencyPairService = currencyPairService;
    }

    @Autowired
    public void setOrderPairService(@Lazy OrderPairService orderPairService) {
        this.orderPairService = orderPairService;
    }



  /*  public void migrateOrder(Long mainOrderId, Long pairId, CryptoCurrencyTransactionStatus status) {
        CurrencyPairDTO pairDTO = currencyPairService.findOne(pairId);
        //update  status

        OrderPairDTO mainOrder = orderPairService.findOne(mainOrderId, pairId, OrderStatusType.IN_PROCESS);
        Map<Long, Long> ids = orderPairService.migrate(mainOrder.getId(), pairId, OrderStatusType.EXECUTED);

        for (Map.Entry<Long, Long> id : ids.entrySet()) {
            cryptoCurrencyTransactionService.migrateTx(pairDTO.getSellId(), id.getKey(), id.getValue());
            cryptoCurrencyTransactionService.migrateTx(pairDTO.getBuyId(), id.getKey(), id.getValue());
        }

    }*/



}
