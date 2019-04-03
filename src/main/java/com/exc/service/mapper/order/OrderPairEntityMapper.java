package com.exc.service.mapper.order;

import com.exc.domain.order.OrderPair;
import com.exc.service.dto.OrderPairDTO;
import com.exc.service.mapper.EntityMapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity OrderPair and its DTO OrderPairDTO.
 */

public interface OrderPairEntityMapper<E extends OrderPair> extends EntityMapper<OrderPairDTO, OrderPair> {
    E fromId(Long id);

    @Mapping(source = "pairId", target = "pair")
    @Mapping(target = "executions", ignore = true)
    @Override
    E toEntity(OrderPairDTO dto);


    @Mapping(source = "pair.id", target = "pairId")
    @Mapping(target = "executions", ignore = true)
    //@Mapping(source = "userInfo.id", target = "userInfoId")
    @Override
    OrderPairDTO toDto(OrderPair entity);

    @Override
    List<OrderPair> toEntity(List<OrderPairDTO> dtoList);

    @Override
    List<OrderPairDTO> toDto(List<OrderPair> entityList);

    Set<OrderPair> toEntity(Set<OrderPairDTO> dtoList);
    Set<OrderPairDTO> toDto(Set<OrderPair> entityList);

  /*  @AfterMapping
    default void fillExecutions(@MappingTarget OrderPairDTO dto, OrderPair orderPair) {
        if (orderPair.getExecutions() != null && orderPair.getExecutions().size() > 0) {
            Set<OrderPairDTO> orderPairDTOS = (Set<OrderPairDTO>) orderPair.getExecutions()
                .stream().map(order -> toDto((OrderPair) order)).collect(Collectors.toSet());

            dto.setExecutions(orderPairDTOS);
        }
    }*/

}
