package com.exc.service;

import com.exc.domain.CryptoCurrency;
import com.exc.repository.CryptoCurrencyRepository;
import com.exc.service.dto.CryptoCurrencyDTO;
import com.exc.service.mapper.CryptoCurrencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CryptoCurrency.
 */
@Service
@Transactional
public class CryptoCurrencyService {

    private final Logger log = LoggerFactory.getLogger(CryptoCurrencyService.class);

    private CryptoCurrencyRepository cryptoCurrencyRepository;

    private CryptoCurrencyMapper cryptoCurrencyMapper;

    public CryptoCurrencyService(CryptoCurrencyRepository cryptoCurrencyRepository, CryptoCurrencyMapper cryptoCurrencyMapper) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
        this.cryptoCurrencyMapper = cryptoCurrencyMapper;
    }

    /**
     * Save a cryptoCurrency.
     *
     * @param cryptoCurrencyDTO the entity to save
     * @return the persisted entity
     */
    public CryptoCurrencyDTO save(CryptoCurrencyDTO cryptoCurrencyDTO) {
        log.debug("Request to save CryptoCurrency : {}", cryptoCurrencyDTO);

        CryptoCurrency cryptoCurrency = cryptoCurrencyMapper.toEntity(cryptoCurrencyDTO);
        cryptoCurrency = cryptoCurrencyRepository.save(cryptoCurrency);
        return cryptoCurrencyMapper.toDto(cryptoCurrency);
    }

    /**
     * Get all the cryptoCurrencies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CryptoCurrencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CryptoCurrencies");
        return cryptoCurrencyRepository.findAll(pageable)
            .map(cryptoCurrencyMapper::toDto);
    }


    /**
     * Get one cryptoCurrency by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CryptoCurrencyDTO> findOne(Long id) {
        log.debug("Request to get CryptoCurrency : {}", id);
        return cryptoCurrencyRepository.findById(id)
            .map(cryptoCurrencyMapper::toDto);
    }

    /**
     * Delete the cryptoCurrency by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CryptoCurrency : {}", id);
        cryptoCurrencyRepository.deleteById(id);
    }
}
