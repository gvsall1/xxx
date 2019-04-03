package com.exc.web.rest;

import com.exc.OrderApp;

import com.exc.config.SecurityBeanOverrideConfiguration;

import com.exc.domain.CurrencyFee;
import com.exc.repository.CurrencyFeeRepository;
import com.exc.service.CurrencyFeeService;
import com.exc.service.dto.CurrencyFeeDTO;
import com.exc.service.mapper.CurrencyFeeMapper;
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
 * Test class for the CurrencyFeeResource REST controller.
 *
 * @see CurrencyFeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, OrderApp.class})
public class CurrencyFeeResourceIntTest {
/*
    private static final BigDecimal DEFAULT_DEPOSIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPOSIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_WITHDRAW = new BigDecimal(1);
    private static final BigDecimal UPDATED_WITHDRAW = new BigDecimal(2);

    @Autowired
    private CurrencyFeeRepository currencyFeeRepository;

    @Autowired
    private CurrencyFeeMapper currencyFeeMapper;
    
    @Autowired
    private CurrencyFeeService currencyFeeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCurrencyFeeMockMvc;

    private CurrencyFee currencyFee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyFeeResource currencyFeeResource = new CurrencyFeeResource(currencyFeeService);
        this.restCurrencyFeeMockMvc = MockMvcBuilders.standaloneSetup(currencyFeeResource)
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
    public static CurrencyFee createEntity(EntityManager em) {
        CurrencyFee currencyFee = new CurrencyFee()
            .deposit(DEFAULT_DEPOSIT)
            .withdraw(DEFAULT_WITHDRAW);
        return currencyFee;
    }

    @Before
    public void initTest() {
        currencyFee = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrencyFee() throws Exception {
        int databaseSizeBeforeCreate = currencyFeeRepository.findAll().size();

        // Create the CurrencyFee
        CurrencyFeeDTO currencyFeeDTO = currencyFeeMapper.toDto(currencyFee);
        restCurrencyFeeMockMvc.perform(post("/api/currency-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyFeeDTO)))
            .andExpect(status().isCreated());

        // Validate the CurrencyFee in the database
        List<CurrencyFee> currencyFeeList = currencyFeeRepository.findAll();
        assertThat(currencyFeeList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyFee testCurrencyFee = currencyFeeList.get(currencyFeeList.size() - 1);
        assertThat(testCurrencyFee.getDeposit()).isEqualTo(DEFAULT_DEPOSIT);
        assertThat(testCurrencyFee.getWithdraw()).isEqualTo(DEFAULT_WITHDRAW);
    }

    @Test
    @Transactional
    public void createCurrencyFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyFeeRepository.findAll().size();

        // Create the CurrencyFee with an existing ID
        currencyFee.setId(1L);
        CurrencyFeeDTO currencyFeeDTO = currencyFeeMapper.toDto(currencyFee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyFeeMockMvc.perform(post("/api/currency-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyFee in the database
        List<CurrencyFee> currencyFeeList = currencyFeeRepository.findAll();
        assertThat(currencyFeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCurrencyFees() throws Exception {
        // Initialize the database
        currencyFeeRepository.saveAndFlush(currencyFee);

        // Get all the currencyFeeList
        restCurrencyFeeMockMvc.perform(get("/api/currency-fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].deposit").value(hasItem(DEFAULT_DEPOSIT.intValue())))
            .andExpect(jsonPath("$.[*].withdraw").value(hasItem(DEFAULT_WITHDRAW.intValue())));
    }
    
    @Test
    @Transactional
    public void getCurrencyFee() throws Exception {
        // Initialize the database
        currencyFeeRepository.saveAndFlush(currencyFee);

        // Get the currencyFee
        restCurrencyFeeMockMvc.perform(get("/api/currency-fees/{id}", currencyFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currencyFee.getId().intValue()))
            .andExpect(jsonPath("$.deposit").value(DEFAULT_DEPOSIT.intValue()))
            .andExpect(jsonPath("$.withdraw").value(DEFAULT_WITHDRAW.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrencyFee() throws Exception {
        // Get the currencyFee
        restCurrencyFeeMockMvc.perform(get("/api/currency-fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrencyFee() throws Exception {
        // Initialize the database
        currencyFeeRepository.saveAndFlush(currencyFee);

        int databaseSizeBeforeUpdate = currencyFeeRepository.findAll().size();

        // Update the currencyFee
        CurrencyFee updatedCurrencyFee = currencyFeeRepository.findById(currencyFee.getId()).get();
        // Disconnect from session so that the updates on updatedCurrencyFee are not directly saved in db
        em.detach(updatedCurrencyFee);
        updatedCurrencyFee
            .deposit(UPDATED_DEPOSIT)
            .withdraw(UPDATED_WITHDRAW);
        CurrencyFeeDTO currencyFeeDTO = currencyFeeMapper.toDto(updatedCurrencyFee);

        restCurrencyFeeMockMvc.perform(put("/api/currency-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyFeeDTO)))
            .andExpect(status().isOk());

        // Validate the CurrencyFee in the database
        List<CurrencyFee> currencyFeeList = currencyFeeRepository.findAll();
        assertThat(currencyFeeList).hasSize(databaseSizeBeforeUpdate);
        CurrencyFee testCurrencyFee = currencyFeeList.get(currencyFeeList.size() - 1);
        assertThat(testCurrencyFee.getDeposit()).isEqualTo(UPDATED_DEPOSIT);
        assertThat(testCurrencyFee.getWithdraw()).isEqualTo(UPDATED_WITHDRAW);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrencyFee() throws Exception {
        int databaseSizeBeforeUpdate = currencyFeeRepository.findAll().size();

        // Create the CurrencyFee
        CurrencyFeeDTO currencyFeeDTO = currencyFeeMapper.toDto(currencyFee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyFeeMockMvc.perform(put("/api/currency-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyFee in the database
        List<CurrencyFee> currencyFeeList = currencyFeeRepository.findAll();
        assertThat(currencyFeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCurrencyFee() throws Exception {
        // Initialize the database
        currencyFeeRepository.saveAndFlush(currencyFee);

        int databaseSizeBeforeDelete = currencyFeeRepository.findAll().size();

        // Get the currencyFee
        restCurrencyFeeMockMvc.perform(delete("/api/currency-fees/{id}", currencyFee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CurrencyFee> currencyFeeList = currencyFeeRepository.findAll();
        assertThat(currencyFeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyFee.class);
        CurrencyFee currencyFee1 = new CurrencyFee();
        currencyFee1.setId(1L);
        CurrencyFee currencyFee2 = new CurrencyFee();
        currencyFee2.setId(currencyFee1.getId());
        assertThat(currencyFee1).isEqualTo(currencyFee2);
        currencyFee2.setId(2L);
        assertThat(currencyFee1).isNotEqualTo(currencyFee2);
        currencyFee1.setId(null);
        assertThat(currencyFee1).isNotEqualTo(currencyFee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyFeeDTO.class);
        CurrencyFeeDTO currencyFeeDTO1 = new CurrencyFeeDTO();
        currencyFeeDTO1.setId(1L);
        CurrencyFeeDTO currencyFeeDTO2 = new CurrencyFeeDTO();
        assertThat(currencyFeeDTO1).isNotEqualTo(currencyFeeDTO2);
        currencyFeeDTO2.setId(currencyFeeDTO1.getId());
        assertThat(currencyFeeDTO1).isEqualTo(currencyFeeDTO2);
        currencyFeeDTO2.setId(2L);
        assertThat(currencyFeeDTO1).isNotEqualTo(currencyFeeDTO2);
        currencyFeeDTO1.setId(null);
        assertThat(currencyFeeDTO1).isNotEqualTo(currencyFeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(currencyFeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(currencyFeeMapper.fromId(null)).isNull();
    }*/
}
