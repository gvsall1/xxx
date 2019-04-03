package com.exc.service;

import com.exc.domain.CurrencyPair;
import com.exc.repository.CurrencyPairRepository;
import com.exc.service.dto.CurrencyPairDTO;
import com.exc.service.mapper.CurrencyPairMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CurrencyPair.
 */
@Service
@Transactional
public class CurrencyPairService {

    private final Logger log = LoggerFactory.getLogger(CurrencyPairService.class);

    private CurrencyPairRepository currencyPairRepository;

    private CurrencyPairMapper currencyPairMapper;

    public CurrencyPairService(CurrencyPairRepository currencyPairRepository, CurrencyPairMapper currencyPairMapper) {
        this.currencyPairRepository = currencyPairRepository;
        this.currencyPairMapper = currencyPairMapper;
    }

    /**
     * Save a currencyPair.
     *
     * @param currencyPairDTO the entity to save
     * @return the persisted entity
     */
    public CurrencyPairDTO save(CurrencyPairDTO currencyPairDTO) {
        log.debug("Request to save CurrencyPair : {}", currencyPairDTO);

        CurrencyPair currencyPair = currencyPairMapper.toEntity(currencyPairDTO);
        currencyPair = currencyPairRepository.save(currencyPair);
        return currencyPairMapper.toDto(currencyPair);
    }

    /**
     * Get all the currencyPairs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CurrencyPairDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyPairs");
        return currencyPairRepository.findAll(pageable)
            .map(currencyPairMapper::toDto);
    }


    /**
     * Get one currencyPair by id.
     *
     * @param id the id of the entity
     * @return the entity
     */

    @Transactional(readOnly = true)
    public CurrencyPairDTO findOne(Long id) {
        log.debug("Request to get CurrencyPair : {}", id);
        CurrencyPair currencyPair = currencyPairRepository.getOne(id);
        return currencyPairMapper.toDto(currencyPair);
    }



    /**
     * Delete the currencyPair by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CurrencyPair : {}", id);
        currencyPairRepository.deleteById(id);
    }
}
