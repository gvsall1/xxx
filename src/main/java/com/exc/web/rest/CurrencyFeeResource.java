package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.service.CurrencyFeeService;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.CurrencyFeeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CurrencyFee.
 */
@RestController
@RequestMapping("/api")
public class CurrencyFeeResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyFeeResource.class);

    private static final String ENTITY_NAME = "orderCurrencyFee";

    private CurrencyFeeService currencyFeeService;

    public CurrencyFeeResource(CurrencyFeeService currencyFeeService) {
        this.currencyFeeService = currencyFeeService;
    }

    /**
     * POST  /currency-fees : Create a new currencyFee.
     *
     * @param currencyFeeDTO the currencyFeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currencyFeeDTO, or with status 400 (Bad Request) if the currencyFee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/currency-fees")
    @Timed
    public ResponseEntity<CurrencyFeeDTO> createCurrencyFee(@RequestBody CurrencyFeeDTO currencyFeeDTO) throws URISyntaxException {
        log.debug("REST request to save CurrencyFee : {}", currencyFeeDTO);
        if (currencyFeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyFee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyFeeDTO result = currencyFeeService.save(currencyFeeDTO);
        return ResponseEntity.created(new URI("/api/currency-fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /currency-fees : Updates an existing currencyFee.
     *
     * @param currencyFeeDTO the currencyFeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currencyFeeDTO,
     * or with status 400 (Bad Request) if the currencyFeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the currencyFeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/currency-fees")
    @Timed
    public ResponseEntity<CurrencyFeeDTO> updateCurrencyFee(@RequestBody CurrencyFeeDTO currencyFeeDTO) throws URISyntaxException {
        log.debug("REST request to update CurrencyFee : {}", currencyFeeDTO);
        if (currencyFeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyFeeDTO result = currencyFeeService.save(currencyFeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currencyFeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currency-fees : get all the currencyFees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencyFees in body
     */
    @GetMapping("/currency-fees")
    @Timed
    public ResponseEntity<List<CurrencyFeeDTO>> getAllCurrencyFees(Pageable pageable) {
        log.debug("REST request to get a page of CurrencyFees");
        Page<CurrencyFeeDTO> page = currencyFeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currency-fees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /currency-fees/:id : get the "id" currencyFee.
     *
     * @param id the id of the currencyFeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currencyFeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/currency-fees/{id}")
    @Timed
    public ResponseEntity<CurrencyFeeDTO> getCurrencyFee(@PathVariable Long id) {
        log.debug("REST request to get CurrencyFee : {}", id);
        CurrencyFeeDTO currencyFeeDTO = currencyFeeService.findOne(id);
        return ResponseEntity.ok(currencyFeeDTO);
    }

    /**
     * DELETE  /currency-fees/:id : delete the "id" currencyFee.
     *
     * @param id the id of the currencyFeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/currency-fees/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrencyFee(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyFee : {}", id);
        currencyFeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
