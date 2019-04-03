package com.exc.service.mapper.operation;

import com.exc.domain.operation.CurrencyOperationEth;
import com.exc.service.mapper.CryptoCurrencyMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CryptoCurrencyMapper.class})
public interface CurrencyOperationEthMapper extends CurrencyOperationEntityMapper<CurrencyOperationEth> {
}
