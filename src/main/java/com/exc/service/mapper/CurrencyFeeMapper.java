package com.exc.service.mapper;

import com.exc.domain.*;
import com.exc.service.dto.CurrencyFeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CurrencyFee and its DTO CurrencyFeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyFeeMapper extends EntityMapper<CurrencyFeeDTO, CurrencyFee> {



    default CurrencyFee fromId(Long id) {
        if (id == null) {
            return null;
        }
        CurrencyFee currencyFee = new CurrencyFee();
        currencyFee.setId(id);
        return currencyFee;
    }
}
