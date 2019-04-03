package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.service.OrderPairService;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.OrderPairDTO;
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
 * REST controller for managing OrderPair.
 */
@RestController
@RequestMapping("/api")
public class OrderPairResource {

    private final Logger log = LoggerFactory.getLogger(OrderPairResource.class);

    private static final String ENTITY_NAME = "orderOrderPair";

    private OrderPairService orderPairService;

    public OrderPairResource(OrderPairService orderPairService) {
        this.orderPairService = orderPairService;
    }

    /**
     * POST  /order-pairs : Create a new orderPair.
     *
     * @param orderPairDTO the orderPairDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderPairDTO, or with status 400 (Bad Request) if the orderPair has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
   /* @PostMapping("/order-pairs")
    @Timed
    public ResponseEntity<OrderPairDTO> createOrderPair(@RequestBody OrderPairDTO orderPairDTO) throws URISyntaxException {
        log.debug("REST request to save OrderPair : {}", orderPairDTO);
        if (orderPairDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderPair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderPairDTO result = orderPairService.save(orderPairDTO);
        return ResponseEntity.created(new URI("/api/order-pairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * PUT  /order-pairs : Updates an existing orderPair.
     *
     * @param orderPairDTO the orderPairDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderPairDTO,
     * or with status 400 (Bad Request) if the orderPairDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderPairDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
  /*  @PutMapping("/order-pairs")
    @Timed
    public ResponseEntity<OrderPairDTO> updateOrderPair(@RequestBody OrderPairDTO orderPairDTO) throws URISyntaxException {
        log.debug("REST request to update OrderPair : {}", orderPairDTO);
        if (orderPairDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderPairDTO result = orderPairService.save(orderPairDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderPairDTO.getId().toString()))
            .body(result);
    }
*/
    /**
     * GET  /order-pairs : get all the orderPairs.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of orderPairs in body
     */
   /* @GetMapping("/order-pairs")
    @Timed
    public ResponseEntity<List<OrderPairDTO>> getAllOrderPairs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of OrderPairs");
        Page<OrderPairDTO> page;
        if (eagerload) {
            page = orderPairService.findAllWithEagerRelationships(pageable);
        } else {
            page = orderPairService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/order-pairs?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
*/
    /**
     * GET  /order-pairs/:id : get the "id" orderPair.
     *
     * @param id the id of the orderPairDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderPairDTO, or with status 404 (Not Found)
     */
/*    @GetMapping("/order-pairs/{id}")
    @Timed
    public ResponseEntity<OrderPairDTO> getOrderPair(@PathVariable Long id) {
        log.debug("REST request to get OrderPair : {}", id);
        Optional<OrderPairDTO> orderPairDTO = orderPairService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderPairDTO);
    }*/


}
