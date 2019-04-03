package com.exc.service.mapper.operation;

import com.exc.domain.operation.CurrencyOperation;
import com.exc.service.dto.CurrencyOperationDTO;
import com.exc.service.mapper.EntityMapper;
import org.mapstruct.Mapping;

import java.util.List;

public interface CurrencyOperationEntityMapper<E extends CurrencyOperation> extends EntityMapper<CurrencyOperationDTO, E> {
    E fromId(Long id);

    @Mapping(source = "currencyId", target = "currency")
    @Override
    E toEntity(CurrencyOperationDTO dto);

    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.currencyName", target = "currencyName")
    @Override
    CurrencyOperationDTO toDto(E entity);

    @Override
    List<E> toEntity(List<CurrencyOperationDTO> dtoList);

    @Override
    List<CurrencyOperationDTO> toDto(List<E> entityList);
}
