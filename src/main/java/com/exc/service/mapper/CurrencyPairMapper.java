package com.exc.service.mapper;

import com.exc.domain.*;
import com.exc.service.dto.CurrencyPairDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CurrencyPair and its DTO CurrencyPairDTO.
 */
@Mapper(componentModel = "spring", uses = {CryptoCurrencyMapper.class, OrderPairFeeMapper.class})
public interface CurrencyPairMapper extends EntityMapper<CurrencyPairDTO, CurrencyPair> {

    @Mapping(source = "buy.id", target = "buyId")
    @Mapping(source = "buy.currencyName", target = "buyName")
    @Mapping(source = "sell.id", target = "sellId")
    @Mapping(source = "sell.currencyName", target = "sellName")
    @Mapping(source = "fee.id", target = "feeId")
    CurrencyPairDTO toDto(CurrencyPair currencyPair);

    @Mapping(source = "buyId", target = "buy")
    @Mapping(source = "sellId", target = "sell")
    @Mapping(source = "feeId", target = "fee")
    CurrencyPair toEntity(CurrencyPairDTO currencyPairDTO);

    default CurrencyPair fromId(Long id) {
        if (id == null) {
            return null;
        }
        CurrencyPair currencyPair = new CurrencyPair();
        currencyPair.setId(id);
        return currencyPair;
    }
}
