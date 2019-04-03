package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.CurrencyOperationDTO;
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
 * REST controller for managing CurrencyOperation.
 */
@RestController
@RequestMapping("/api")
public class CurrencyOperationResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyOperationResource.class);

    private static final String ENTITY_NAME = "orderCurrencyOperation";

 /*   private CurrencyOperationService currencyOperationService;

    public CurrencyOperationResource(CurrencyOperationService currencyOperationService) {
        this.currencyOperationService = currencyOperationService;
    }

    *//**
     * POST  /currency-operations : Create a new currencyOperation.
     *
     * @param currencyOperationDTO the currencyOperationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currencyOperationDTO, or with status 400 (Bad Request) if the currencyOperation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     *//*
    @PostMapping("/currency-operations")
    @Timed
    public ResponseEntity<CurrencyOperationDTO> createCurrencyOperation(@RequestBody CurrencyOperationDTO currencyOperationDTO) throws URISyntaxException {
        log.debug("REST request to save CurrencyOperation : {}", currencyOperationDTO);
        if (currencyOperationDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyOperation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyOperationDTO result = currencyOperationService.save(currencyOperationDTO);
        return ResponseEntity.created(new URI("/api/currency-operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    *//**
     * PUT  /currency-operations : Updates an existing currencyOperation.
     *
     * @param currencyOperationDTO the currencyOperationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currencyOperationDTO,
     * or with status 400 (Bad Request) if the currencyOperationDTO is not valid,
     * or with status 500 (Internal Server Error) if the currencyOperationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     *//*
    @PutMapping("/currency-operations")
    @Timed
    public ResponseEntity<CurrencyOperationDTO> updateCurrencyOperation(@RequestBody CurrencyOperationDTO currencyOperationDTO) throws URISyntaxException {
        log.debug("REST request to update CurrencyOperation : {}", currencyOperationDTO);
        if (currencyOperationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyOperationDTO result = currencyOperationService.save(currencyOperationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currencyOperationDTO.getId().toString()))
            .body(result);
    }

    *//**
     * GET  /currency-operations : get all the currencyOperations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencyOperations in body
     *//*
    @GetMapping("/currency-operations")
    @Timed
    public ResponseEntity<List<CurrencyOperationDTO>> getAllCurrencyOperations(Pageable pageable) {
        log.debug("REST request to get a page of CurrencyOperations");
        Page<CurrencyOperationDTO> page = currencyOperationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currency-operations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    *//**
     * GET  /currency-operations/:id : get the "id" currencyOperation.
     *
     * @param id the id of the currencyOperationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currencyOperationDTO, or with status 404 (Not Found)
     *//*
    @GetMapping("/currency-operations/{id}")
    @Timed
    public ResponseEntity<CurrencyOperationDTO> getCurrencyOperation(@PathVariable Long id) {
        log.debug("REST request to get CurrencyOperation : {}", id);
        Optional<CurrencyOperationDTO> currencyOperationDTO = currencyOperationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyOperationDTO);
    }

    *//**
     * DELETE  /currency-operations/:id : delete the "id" currencyOperation.
     *
     * @param id the id of the currencyOperationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     *//*
    @DeleteMapping("/currency-operations/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrencyOperation(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyOperation : {}", id);
        currencyOperationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }*/
}
