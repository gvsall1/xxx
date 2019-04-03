package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.service.CurrencyPairService;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.CurrencyPairDTO;
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
 * REST controller for managing CurrencyPair.
 */
@RestController
@RequestMapping("/api")
public class CurrencyPairResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyPairResource.class);

    private static final String ENTITY_NAME = "orderCurrencyPair";

    private CurrencyPairService currencyPairService;

    public CurrencyPairResource(CurrencyPairService currencyPairService) {
        this.currencyPairService = currencyPairService;
    }

    /**
     * POST  /currency-pairs : Create a new currencyPair.
     *
     * @param currencyPairDTO the currencyPairDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currencyPairDTO, or with status 400 (Bad Request) if the currencyPair has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/currency-pairs")
    @Timed
    public ResponseEntity<CurrencyPairDTO> createCurrencyPair(@RequestBody CurrencyPairDTO currencyPairDTO) throws URISyntaxException {
        log.debug("REST request to save CurrencyPair : {}", currencyPairDTO);
        if (currencyPairDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyPair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyPairDTO result = currencyPairService.save(currencyPairDTO);
        return ResponseEntity.created(new URI("/api/currency-pairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /currency-pairs : Updates an existing currencyPair.
     *
     * @param currencyPairDTO the currencyPairDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currencyPairDTO,
     * or with status 400 (Bad Request) if the currencyPairDTO is not valid,
     * or with status 500 (Internal Server Error) if the currencyPairDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/currency-pairs")
    @Timed
    public ResponseEntity<CurrencyPairDTO> updateCurrencyPair(@RequestBody CurrencyPairDTO currencyPairDTO) throws URISyntaxException {
        log.debug("REST request to update CurrencyPair : {}", currencyPairDTO);
        if (currencyPairDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyPairDTO result = currencyPairService.save(currencyPairDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currencyPairDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currency-pairs : get all the currencyPairs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencyPairs in body
     */
    @GetMapping("/currency-pairs")
    @Timed
    public ResponseEntity<List<CurrencyPairDTO>> getAllCurrencyPairs(Pageable pageable) {
        log.debug("REST request to get a page of CurrencyPairs");
        Page<CurrencyPairDTO> page = currencyPairService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currency-pairs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /currency-pairs/:id : get the "id" currencyPair.
     *
     * @param id the id of the currencyPairDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currencyPairDTO, or with status 404 (Not Found)
     */
    @GetMapping("/currency-pairs/{id}")
    @Timed
    public ResponseEntity<CurrencyPairDTO> getCurrencyPair(@PathVariable Long id) {
        log.debug("REST request to get CurrencyPair : {}", id);
        CurrencyPairDTO currencyPairDTO = currencyPairService.findOne(id);
        return ResponseEntity.ok(currencyPairDTO);
    }

    /**
     * DELETE  /currency-pairs/:id : delete the "id" currencyPair.
     *
     * @param id the id of the currencyPairDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/currency-pairs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrencyPair(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyPair : {}", id);
        currencyPairService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
