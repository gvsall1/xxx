package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.service.OrderPairFeeService;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.OrderPairFeeDTO;
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
 * REST controller for managing OrderPairFee.
 */
@RestController
@RequestMapping("/api")
public class OrderPairFeeResource {

    private final Logger log = LoggerFactory.getLogger(OrderPairFeeResource.class);

    private static final String ENTITY_NAME = "orderOrderPairFee";

    private OrderPairFeeService orderPairFeeService;

    public OrderPairFeeResource(OrderPairFeeService orderPairFeeService) {
        this.orderPairFeeService = orderPairFeeService;
    }

    /**
     * POST  /order-pair-fees : Create a new orderPairFee.
     *
     * @param orderPairFeeDTO the orderPairFeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderPairFeeDTO, or with status 400 (Bad Request) if the orderPairFee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-pair-fees")
    @Timed
    public ResponseEntity<OrderPairFeeDTO> createOrderPairFee(@RequestBody OrderPairFeeDTO orderPairFeeDTO) throws URISyntaxException {
        log.debug("REST request to save OrderPairFee : {}", orderPairFeeDTO);
        if (orderPairFeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderPairFee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderPairFeeDTO result = orderPairFeeService.save(orderPairFeeDTO);
        return ResponseEntity.created(new URI("/api/order-pair-fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-pair-fees : Updates an existing orderPairFee.
     *
     * @param orderPairFeeDTO the orderPairFeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderPairFeeDTO,
     * or with status 400 (Bad Request) if the orderPairFeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderPairFeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-pair-fees")
    @Timed
    public ResponseEntity<OrderPairFeeDTO> updateOrderPairFee(@RequestBody OrderPairFeeDTO orderPairFeeDTO) throws URISyntaxException {
        log.debug("REST request to update OrderPairFee : {}", orderPairFeeDTO);
        if (orderPairFeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderPairFeeDTO result = orderPairFeeService.save(orderPairFeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderPairFeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-pair-fees : get all the orderPairFees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderPairFees in body
     */
    @GetMapping("/order-pair-fees")
    @Timed
    public ResponseEntity<List<OrderPairFeeDTO>> getAllOrderPairFees(Pageable pageable) {
        log.debug("REST request to get a page of OrderPairFees");
        Page<OrderPairFeeDTO> page = orderPairFeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-pair-fees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-pair-fees/:id : get the "id" orderPairFee.
     *
     * @param id the id of the orderPairFeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderPairFeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/order-pair-fees/{id}")
    @Timed
    public ResponseEntity<OrderPairFeeDTO> getOrderPairFee(@PathVariable Long id) {
        log.debug("REST request to get OrderPairFee : {}", id);
        OrderPairFeeDTO orderPairFeeDTO = orderPairFeeService.findOne(id);
        return new ResponseEntity<>(orderPairFeeDTO, HttpStatus.OK);
    }

    /**
     * DELETE  /order-pair-fees/:id : delete the "id" orderPairFee.
     *
     * @param id the id of the orderPairFeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-pair-fees/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderPairFee(@PathVariable Long id) {
        log.debug("REST request to delete OrderPairFee : {}", id);
        orderPairFeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
