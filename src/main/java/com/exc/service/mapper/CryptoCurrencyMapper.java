package com.exc.service.mapper;

import com.exc.domain.*;
import com.exc.service.dto.CryptoCurrencyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CryptoCurrency and its DTO CryptoCurrencyDTO.
 */
@Mapper(componentModel = "spring", uses = {CurrencyFeeMapper.class})
public interface CryptoCurrencyMapper extends EntityMapper<CryptoCurrencyDTO, CryptoCurrency> {

    @Mapping(source = "fee.id", target = "feeId")
    CryptoCurrencyDTO toDto(CryptoCurrency cryptoCurrency);

    @Mapping(source = "feeId", target = "fee")
    CryptoCurrency toEntity(CryptoCurrencyDTO cryptoCurrencyDTO);

    default CryptoCurrency fromId(Long id) {
        if (id == null) {
            return null;
        }
        CryptoCurrency cryptoCurrency = new CryptoCurrency();
        cryptoCurrency.setId(id);
        return cryptoCurrency;
    }
}
