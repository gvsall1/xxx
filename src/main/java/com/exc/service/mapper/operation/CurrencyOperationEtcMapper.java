package com.exc.service.mapper.operation;

import com.exc.domain.operation.CurrencyOperationEtc;
import com.exc.service.mapper.CryptoCurrencyMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CryptoCurrencyMapper.class})
public interface CurrencyOperationEtcMapper extends CurrencyOperationEntityMapper<CurrencyOperationEtc> {
}
