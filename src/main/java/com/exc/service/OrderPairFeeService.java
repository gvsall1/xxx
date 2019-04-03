package com.exc.service;

import com.exc.domain.CryptoCurrency;
import com.exc.domain.OrderPairFee;
import com.exc.domain.enumeration.OrderStatusType;
import com.exc.domain.enumeration.OrderType;
import com.exc.domain.order.OrderPair;
import com.exc.repository.order.OrderPairFeeRepository;
import com.exc.service.dto.OrderPairFeeDTO;
import com.exc.service.dto.remote.WithdrawRequestDTO;
import com.exc.service.mapper.OrderPairFeeMapper;
import com.exc.service.remote.TxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;


/**
 * Service Implementation for managing OrderPairFee.
 */
@Service
@Transactional
public class OrderPairFeeService {

    private final Logger log = LoggerFactory.getLogger(OrderPairFeeService.class);

    private OrderPairFeeRepository orderPairFeeRepository;
    private OrderPairFeeMapper orderPairFeeMapper;
    private TxService txService;

    public OrderPairFeeService(OrderPairFeeRepository orderPairFeeRepository, OrderPairFeeMapper orderPairFeeMapper, TxService txService) {
        this.orderPairFeeRepository = orderPairFeeRepository;
        this.orderPairFeeMapper = orderPairFeeMapper;
        this.txService = txService;
    }

    /**
     * Save a orderPairFee.
     *
     * @param orderPairFeeDTO the entity to save
     * @return the persisted entity
     */
    public OrderPairFeeDTO save(OrderPairFeeDTO orderPairFeeDTO) {
        log.debug("Request to save OrderPairFee : {}", orderPairFeeDTO);
        OrderPairFee orderPairFee = orderPairFeeMapper.toEntity(orderPairFeeDTO);
        orderPairFee = orderPairFeeRepository.save(orderPairFee);
        return orderPairFeeMapper.toDto(orderPairFee);
    }

    /**
     * Get all the orderPairFees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderPairFeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderPairFees");
        return orderPairFeeRepository.findAll(pageable)
            .map(orderPairFeeMapper::toDto);
    }

    /**
     * Get one orderPairFee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderPairFeeDTO findOne(Long id) {
        log.debug("Request to get OrderPairFee : {}", id);
        OrderPairFee orderPairFee = orderPairFeeRepository.getOne(id);
        return orderPairFeeMapper.toDto(orderPairFee);
    }

    /**
     * Delete the orderPairFee by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderPairFee : {}", id);
        orderPairFeeRepository.deleteById(id);
    }

    /**
     * Withdraw fee from orderPair operation
     *
     * @param orderPair
     * @param type
     * @return null, or id of transaction from txService microservice
     */
    public Long withdrawOrderFee(OrderPair orderPair, OrderStatusType type, String fromKey) {
        OrderPairFee fee = orderPair.getPair().getFee();
        log.debug("Request to withdraw fee for order {}", orderPair.getId());
        Long res = null;
        if (fee != null) {
            BigInteger orderFee = null;

            if (type == OrderStatusType.NEW) orderFee = fee.getPlaceOrder();
            if (type == OrderStatusType.MODIFIED) orderFee = fee.getModifyOrder();
            if (type == OrderStatusType.CANCELLED) orderFee = fee.getDeleteOrder();

            if (orderFee.compareTo(new BigInteger("0")) > 0) {
                log.info("Withdrawing fee from order : {}", orderPair.getId());
                CryptoCurrency feeCurrency = orderPair.getType() == OrderType.BUY ?
                    orderPair.getPair().getBuy() : orderPair.getPair().getSell();
                WithdrawRequestDTO withdrawRequestDTO = new WithdrawRequestDTO();
                withdrawRequestDTO.setExternalId(orderPair.getId());
                withdrawRequestDTO.setFee(orderFee);
                withdrawRequestDTO.setFromPrivate(fromKey);
                withdrawRequestDTO.setCurrencyName(feeCurrency.getCurrencyName());
                res = txService.withdrawOrderFee(withdrawRequestDTO);
            }
        }

        return res;

    }


}
