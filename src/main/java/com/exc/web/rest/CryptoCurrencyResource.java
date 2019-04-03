package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.service.CryptoCurrencyService;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.CryptoCurrencyDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CryptoCurrency.
 */
@RestController
@RequestMapping("/api")
public class CryptoCurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CryptoCurrencyResource.class);

    private static final String ENTITY_NAME = "orderCryptoCurrency";

    private CryptoCurrencyService cryptoCurrencyService;

    public CryptoCurrencyResource(CryptoCurrencyService cryptoCurrencyService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
    }

    /**
     * POST  /crypto-currencies : Create a new cryptoCurrency.
     *
     * @param cryptoCurrencyDTO the cryptoCurrencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cryptoCurrencyDTO, or with status 400 (Bad Request) if the cryptoCurrency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/crypto-currencies")
    @Timed
    public ResponseEntity<CryptoCurrencyDTO> createCryptoCurrency(@Valid @RequestBody CryptoCurrencyDTO cryptoCurrencyDTO) throws URISyntaxException {
        log.debug("REST request to save CryptoCurrency : {}", cryptoCurrencyDTO);
        if (cryptoCurrencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new cryptoCurrency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CryptoCurrencyDTO result = cryptoCurrencyService.save(cryptoCurrencyDTO);
        return ResponseEntity.created(new URI("/api/crypto-currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /crypto-currencies : Updates an existing cryptoCurrency.
     *
     * @param cryptoCurrencyDTO the cryptoCurrencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cryptoCurrencyDTO,
     * or with status 400 (Bad Request) if the cryptoCurrencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the cryptoCurrencyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/crypto-currencies")
    @Timed
    public ResponseEntity<CryptoCurrencyDTO> updateCryptoCurrency(@Valid @RequestBody CryptoCurrencyDTO cryptoCurrencyDTO) throws URISyntaxException {
        log.debug("REST request to update CryptoCurrency : {}", cryptoCurrencyDTO);
        if (cryptoCurrencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CryptoCurrencyDTO result = cryptoCurrencyService.save(cryptoCurrencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cryptoCurrencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /crypto-currencies : get all the cryptoCurrencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cryptoCurrencies in body
     */
    @GetMapping("/crypto-currencies")
    @Timed
    public ResponseEntity<List<CryptoCurrencyDTO>> getAllCryptoCurrencies(Pageable pageable) {
        log.debug("REST request to get a page of CryptoCurrencies");
        Page<CryptoCurrencyDTO> page = cryptoCurrencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/crypto-currencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /crypto-currencies/:id : get the "id" cryptoCurrency.
     *
     * @param id the id of the cryptoCurrencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cryptoCurrencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/crypto-currencies/{id}")
    @Timed
    public ResponseEntity<CryptoCurrencyDTO> getCryptoCurrency(@PathVariable Long id) {
        log.debug("REST request to get CryptoCurrency : {}", id);
        Optional<CryptoCurrencyDTO> cryptoCurrencyDTO = cryptoCurrencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cryptoCurrencyDTO);
    }

    /**
     * DELETE  /crypto-currencies/:id : delete the "id" cryptoCurrency.
     *
     * @param id the id of the cryptoCurrencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/crypto-currencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCryptoCurrency(@PathVariable Long id) {
        log.debug("REST request to delete CryptoCurrency : {}", id);
        cryptoCurrencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
