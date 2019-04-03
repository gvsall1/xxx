package com.exc.repository.operation;

import com.exc.domain.CurrencyName;
import org.springframework.stereotype.Component;

@Component
public class CurrencyOperationRepositoryFactory {
    private final CurrencyOperationEthRepository currencyOperationEthRepository;
    private final CurrencyOperationBtcRepository currencyOperationBtcRepository;
    private final CurrencyOperationEtcRepository currencyOperationEtcRepository;

    public CurrencyOperationRepositoryFactory(CurrencyOperationEthRepository currencyOperationEthRepository, CurrencyOperationBtcRepository currencyOperationBtcRepository, CurrencyOperationEtcRepository currencyOperationEtcRepository) {
        this.currencyOperationEthRepository = currencyOperationEthRepository;
        this.currencyOperationBtcRepository = currencyOperationBtcRepository;
        this.currencyOperationEtcRepository = currencyOperationEtcRepository;
    }


    public CurrencyOperationRepository getRepo(CurrencyName currency) {
        CurrencyOperationRepository res = null;
        switch (currency) {
            case ETH:
                res = currencyOperationEthRepository;
                break;
            case ETC:
                res = currencyOperationEtcRepository;
                break;
            case BTC:
                res = currencyOperationBtcRepository;
                break;
        }
        return res;
    }
}
