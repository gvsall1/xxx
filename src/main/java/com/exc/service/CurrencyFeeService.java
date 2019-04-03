package com.exc.service;

import com.exc.config.ApplicationProperties;
import com.exc.domain.CryptoCurrency;
import com.exc.domain.CurrencyFee;
import com.exc.domain.enumeration.CryptoCurrencyTransactionStatus;
import com.exc.domain.enumeration.CryptoCurrencyTransactionType;
import com.exc.domain.enumeration.OperationType;
import com.exc.repository.CryptoCurrencyRepository;
import com.exc.repository.CurrencyFeeRepository;
import com.exc.service.dto.CurrencyFeeDTO;
import com.exc.service.mapper.CurrencyFeeMapper;
import com.exc.service.remote.TxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;


/**
 * Service Implementation for managing CurrencyFee.
 */
@Service
@Transactional
public class CurrencyFeeService {

    private final Logger log = LoggerFactory.getLogger(CurrencyFeeService.class);

    private CurrencyFeeRepository currencyFeeRepository;
    private CurrencyFeeMapper currencyFeeMapper;
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    public CurrencyFeeService(CurrencyFeeRepository currencyFeeRepository, CurrencyFeeMapper currencyFeeMapper, CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.currencyFeeRepository = currencyFeeRepository;
        this.currencyFeeMapper = currencyFeeMapper;
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }

    /**
     * Save a currencyFee.
     *
     * @param currencyFeeDTO the entity to save
     * @return the persisted entity
     */
    public CurrencyFeeDTO save(CurrencyFeeDTO currencyFeeDTO) {
        log.debug("Request to save CurrencyFee : {}", currencyFeeDTO);
        CurrencyFee currencyFee = currencyFeeMapper.toEntity(currencyFeeDTO);
        currencyFee = currencyFeeRepository.save(currencyFee);
        return currencyFeeMapper.toDto(currencyFee);
    }

    /**
     * Get all the currencyFees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CurrencyFeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyFees");
        return currencyFeeRepository.findAll(pageable)
            .map(currencyFeeMapper::toDto);
    }

    /**
     * Get one currencyFee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CurrencyFeeDTO findOne(Long id) {
        log.debug("Request to get CurrencyFee : {}", id);
        CurrencyFee currencyFee = currencyFeeRepository.getOne(id);
        return currencyFeeMapper.toDto(currencyFee);
    }

    /**
     * Delete the currencyFee by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CurrencyFee : {}", id);
        currencyFeeRepository.deleteById(id);
    }

    /**
     * @param currencyId
     * @param opType
     * @return null in case fee not found
     */
    public BigInteger getFee(Long currencyId, OperationType opType) {
        log.debug("Request to withdrawOPFee type {}", opType);
        BigInteger res = null;
        CryptoCurrency currency = cryptoCurrencyRepository.getOne(currencyId);
        CurrencyFee fee = currency.getFee();
        switch (opType) {
            case WITHDRAW:
                res = fee.getWithdraw();
                break;
            case DEPOSIT:
                res = fee.getDeposit();
                break;
        }
        if (res == null) {
            log.warn("Ignored withdrawOPFee : {}, fee not found ", currency.getCurrencyName());
        }

        return res;
    }
}
