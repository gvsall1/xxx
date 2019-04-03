package com.exc.service.mapper.operation;

import com.exc.domain.CurrencyName;
import org.springframework.stereotype.Component;

@Component
public class CurrencyOperationMapperFactory {
    private final CurrencyOperationEtcMapper currencyOperationEtcMapper;
    private final CurrencyOperationEthMapper currencyOperationEthMapper;
    private final CurrencyOperationBtcMapper currencyOperationBtcMapper;

    public CurrencyOperationMapperFactory(CurrencyOperationEtcMapper currencyOperationEtcMapper, CurrencyOperationEthMapper currencyOperationEthMapper, CurrencyOperationBtcMapper currencyOperationBtcMapper) {
        this.currencyOperationEtcMapper = currencyOperationEtcMapper;
        this.currencyOperationEthMapper = currencyOperationEthMapper;
        this.currencyOperationBtcMapper = currencyOperationBtcMapper;
    }

    public CurrencyOperationEntityMapper getMapper(CurrencyName currency) {
        CurrencyOperationEntityMapper res = null;
        switch (currency) {
            case ETH:
                res = currencyOperationEthMapper;
                break;
            case ETC:
                res = currencyOperationEtcMapper;
                break;
            case BTC:
                res = currencyOperationBtcMapper;
                break;
        }


        return res;

    }
}
