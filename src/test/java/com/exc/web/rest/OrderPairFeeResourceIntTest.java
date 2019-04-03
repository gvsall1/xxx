package com.exc.web.rest;

import com.exc.OrderApp;

import com.exc.config.SecurityBeanOverrideConfiguration;

import com.exc.domain.OrderPairFee;
import com.exc.service.OrderPairFeeService;
import com.exc.service.dto.OrderPairFeeDTO;
import com.exc.service.mapper.OrderPairFeeMapper;
import com.exc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;


import static com.exc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderPairFeeResource REST controller.
 *
 * @see OrderPairFeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, OrderApp.class})
public class OrderPairFeeResourceIntTest {
/*
    private static final BigDecimal DEFAULT_PLACE_ORDER = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLACE_ORDER = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MODIFY_ORDER = new BigDecimal(1);
    private static final BigDecimal UPDATED_MODIFY_ORDER = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DELETE_ORDER = new BigDecimal(1);
    private static final BigDecimal UPDATED_DELETE_ORDER = new BigDecimal(2);

    @Autowired
    private OrderPairFeeRepository orderPairFeeRepository;

    @Autowired
    private OrderPairFeeMapper orderPairFeeMapper;
    
    @Autowired
    private OrderPairFeeService orderPairFeeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderPairFeeMockMvc;

    private OrderPairFee orderPairFee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderPairFeeResource orderPairFeeResource = new OrderPairFeeResource(orderPairFeeService);
        this.restOrderPairFeeMockMvc = MockMvcBuilders.standaloneSetup(orderPairFeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static OrderPairFee createEntity(EntityManager em) {
        OrderPairFee orderPairFee = new OrderPairFee()
            .placeOrder(DEFAULT_PLACE_ORDER)
            .modifyOrder(DEFAULT_MODIFY_ORDER)
            .deleteOrder(DEFAULT_DELETE_ORDER);
        return orderPairFee;
    }

    @Before
    public void initTest() {
        orderPairFee = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderPairFee() throws Exception {
        int databaseSizeBeforeCreate = orderPairFeeRepository.findAll().size();

        // Create the OrderPairFee
        OrderPairFeeDTO orderPairFeeDTO = orderPairFeeMapper.toDto(orderPairFee);
        restOrderPairFeeMockMvc.perform(post("/api/order-pair-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairFeeDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderPairFee in the database
        List<OrderPairFee> orderPairFeeList = orderPairFeeRepository.findAll();
        assertThat(orderPairFeeList).hasSize(databaseSizeBeforeCreate + 1);
        OrderPairFee testOrderPairFee = orderPairFeeList.get(orderPairFeeList.size() - 1);
        assertThat(testOrderPairFee.getPlaceOrder()).isEqualTo(DEFAULT_PLACE_ORDER);
        assertThat(testOrderPairFee.getModifyOrder()).isEqualTo(DEFAULT_MODIFY_ORDER);
        assertThat(testOrderPairFee.getDeleteOrder()).isEqualTo(DEFAULT_DELETE_ORDER);
    }

    @Test
    @Transactional
    public void createOrderPairFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderPairFeeRepository.findAll().size();

        // Create the OrderPairFee with an existing ID
        orderPairFee.setId(1L);
        OrderPairFeeDTO orderPairFeeDTO = orderPairFeeMapper.toDto(orderPairFee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderPairFeeMockMvc.perform(post("/api/order-pair-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPairFee in the database
        List<OrderPairFee> orderPairFeeList = orderPairFeeRepository.findAll();
        assertThat(orderPairFeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrderPairFees() throws Exception {
        // Initialize the database
        orderPairFeeRepository.saveAndFlush(orderPairFee);

        // Get all the orderPairFeeList
        restOrderPairFeeMockMvc.perform(get("/api/order-pair-fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderPairFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].placeOrder").value(hasItem(DEFAULT_PLACE_ORDER.intValue())))
            .andExpect(jsonPath("$.[*].modifyOrder").value(hasItem(DEFAULT_MODIFY_ORDER.intValue())))
            .andExpect(jsonPath("$.[*].deleteOrder").value(hasItem(DEFAULT_DELETE_ORDER.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrderPairFee() throws Exception {
        // Initialize the database
        orderPairFeeRepository.saveAndFlush(orderPairFee);

        // Get the orderPairFee
        restOrderPairFeeMockMvc.perform(get("/api/order-pair-fees/{id}", orderPairFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderPairFee.getId().intValue()))
            .andExpect(jsonPath("$.placeOrder").value(DEFAULT_PLACE_ORDER.intValue()))
            .andExpect(jsonPath("$.modifyOrder").value(DEFAULT_MODIFY_ORDER.intValue()))
            .andExpect(jsonPath("$.deleteOrder").value(DEFAULT_DELETE_ORDER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderPairFee() throws Exception {
        // Get the orderPairFee
        restOrderPairFeeMockMvc.perform(get("/api/order-pair-fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderPairFee() throws Exception {
        // Initialize the database
        orderPairFeeRepository.saveAndFlush(orderPairFee);

        int databaseSizeBeforeUpdate = orderPairFeeRepository.findAll().size();

        // Update the orderPairFee
        OrderPairFee updatedOrderPairFee = orderPairFeeRepository.findById(orderPairFee.getId()).get();
        // Disconnect from session so that the updates on updatedOrderPairFee are not directly saved in db
        em.detach(updatedOrderPairFee);
        updatedOrderPairFee
            .placeOrder(UPDATED_PLACE_ORDER)
            .modifyOrder(UPDATED_MODIFY_ORDER)
            .deleteOrder(UPDATED_DELETE_ORDER);
        OrderPairFeeDTO orderPairFeeDTO = orderPairFeeMapper.toDto(updatedOrderPairFee);

        restOrderPairFeeMockMvc.perform(put("/api/order-pair-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairFeeDTO)))
            .andExpect(status().isOk());

        // Validate the OrderPairFee in the database
        List<OrderPairFee> orderPairFeeList = orderPairFeeRepository.findAll();
        assertThat(orderPairFeeList).hasSize(databaseSizeBeforeUpdate);
        OrderPairFee testOrderPairFee = orderPairFeeList.get(orderPairFeeList.size() - 1);
        assertThat(testOrderPairFee.getPlaceOrder()).isEqualTo(UPDATED_PLACE_ORDER);
        assertThat(testOrderPairFee.getModifyOrder()).isEqualTo(UPDATED_MODIFY_ORDER);
        assertThat(testOrderPairFee.getDeleteOrder()).isEqualTo(UPDATED_DELETE_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderPairFee() throws Exception {
        int databaseSizeBeforeUpdate = orderPairFeeRepository.findAll().size();

        // Create the OrderPairFee
        OrderPairFeeDTO orderPairFeeDTO = orderPairFeeMapper.toDto(orderPairFee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderPairFeeMockMvc.perform(put("/api/order-pair-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPairFee in the database
        List<OrderPairFee> orderPairFeeList = orderPairFeeRepository.findAll();
        assertThat(orderPairFeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderPairFee() throws Exception {
        // Initialize the database
        orderPairFeeRepository.saveAndFlush(orderPairFee);

        int databaseSizeBeforeDelete = orderPairFeeRepository.findAll().size();

        // Get the orderPairFee
        restOrderPairFeeMockMvc.perform(delete("/api/order-pair-fees/{id}", orderPairFee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderPairFee> orderPairFeeList = orderPairFeeRepository.findAll();
        assertThat(orderPairFeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderPairFee.class);
        OrderPairFee orderPairFee1 = new OrderPairFee();
        orderPairFee1.setId(1L);
        OrderPairFee orderPairFee2 = new OrderPairFee();
        orderPairFee2.setId(orderPairFee1.getId());
        assertThat(orderPairFee1).isEqualTo(orderPairFee2);
        orderPairFee2.setId(2L);
        assertThat(orderPairFee1).isNotEqualTo(orderPairFee2);
        orderPairFee1.setId(null);
        assertThat(orderPairFee1).isNotEqualTo(orderPairFee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderPairFeeDTO.class);
        OrderPairFeeDTO orderPairFeeDTO1 = new OrderPairFeeDTO();
        orderPairFeeDTO1.setId(1L);
        OrderPairFeeDTO orderPairFeeDTO2 = new OrderPairFeeDTO();
        assertThat(orderPairFeeDTO1).isNotEqualTo(orderPairFeeDTO2);
        orderPairFeeDTO2.setId(orderPairFeeDTO1.getId());
        assertThat(orderPairFeeDTO1).isEqualTo(orderPairFeeDTO2);
        orderPairFeeDTO2.setId(2L);
        assertThat(orderPairFeeDTO1).isNotEqualTo(orderPairFeeDTO2);
        orderPairFeeDTO1.setId(null);
        assertThat(orderPairFeeDTO1).isNotEqualTo(orderPairFeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(orderPairFeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(orderPairFeeMapper.fromId(null)).isNull();
    }*/
}
