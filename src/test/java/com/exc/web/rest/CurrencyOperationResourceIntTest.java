package com.exc.web.rest;

import com.exc.OrderApp;

import com.exc.config.SecurityBeanOverrideConfiguration;

import com.exc.service.CurrencyOperationService;
import com.exc.service.dto.CurrencyOperationDTO;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.exc.web.rest.TestUtil.sameInstant;
import static com.exc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exc.domain.enumeration.OperationType;
/**
 * Test class for the CurrencyOperationResource REST controller.
 *
 * @see CurrencyOperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, OrderApp.class})
public class CurrencyOperationResourceIntTest {

    private static final OperationType DEFAULT_TYPE = OperationType.DEPOSIT;
    private static final OperationType UPDATED_TYPE = OperationType.WITHDRAW;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_VALIDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALIDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VALID = false;
    private static final Boolean UPDATED_IS_VALID = true;

    private static final String DEFAULT_TX = "AAAAAAAAAA";
    private static final String UPDATED_TX = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
/*
    @Autowired
    private CurrencyOperationRepository currencyOperationRepository;

    @Autowired
    private CurrencyOperationMapper currencyOperationMapper;
    
    @Autowired
    private CurrencyOperationService currencyOperationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCurrencyOperationMockMvc;

    private CurrencyOperation currencyOperation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyOperationResource currencyOperationResource = new CurrencyOperationResource(currencyOperationService);
        this.restCurrencyOperationMockMvc = MockMvcBuilders.standaloneSetup(currencyOperationResource)
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
    public static CurrencyOperation createEntity(EntityManager em) {
        CurrencyOperation currencyOperation = new CurrencyOperation()
            .type(DEFAULT_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .validateDate(DEFAULT_VALIDATE_DATE)
            .message(DEFAULT_MESSAGE)
            .isValid(DEFAULT_IS_VALID)
            .tx(DEFAULT_TX)
            .receiverAddress(DEFAULT_RECEIVER_ADDRESS)
            .value(DEFAULT_VALUE);
        return currencyOperation;
    }

    @Before
    public void initTest() {
        currencyOperation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrencyOperation() throws Exception {
        int databaseSizeBeforeCreate = currencyOperationRepository.findAll().size();

        // Create the CurrencyOperation
        CurrencyOperationDTO currencyOperationDTO = currencyOperationMapper.toDto(currencyOperation);
        restCurrencyOperationMockMvc.perform(post("/api/currency-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyOperationDTO)))
            .andExpect(status().isCreated());

        // Validate the CurrencyOperation in the database
        List<CurrencyOperation> currencyOperationList = currencyOperationRepository.findAll();
        assertThat(currencyOperationList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyOperation testCurrencyOperation = currencyOperationList.get(currencyOperationList.size() - 1);
        assertThat(testCurrencyOperation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCurrencyOperation.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCurrencyOperation.getValidateDate()).isEqualTo(DEFAULT_VALIDATE_DATE);
        assertThat(testCurrencyOperation.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCurrencyOperation.isIsValid()).isEqualTo(DEFAULT_IS_VALID);
        assertThat(testCurrencyOperation.getTx()).isEqualTo(DEFAULT_TX);
        assertThat(testCurrencyOperation.getReceiverAddress()).isEqualTo(DEFAULT_RECEIVER_ADDRESS);
        assertThat(testCurrencyOperation.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createCurrencyOperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyOperationRepository.findAll().size();

        // Create the CurrencyOperation with an existing ID
        currencyOperation.setId(1L);
        CurrencyOperationDTO currencyOperationDTO = currencyOperationMapper.toDto(currencyOperation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyOperationMockMvc.perform(post("/api/currency-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyOperationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyOperation in the database
        List<CurrencyOperation> currencyOperationList = currencyOperationRepository.findAll();
        assertThat(currencyOperationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCurrencyOperations() throws Exception {
        // Initialize the database
        currencyOperationRepository.saveAndFlush(currencyOperation);

        // Get all the currencyOperationList
        restCurrencyOperationMockMvc.perform(get("/api/currency-operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].validateDate").value(hasItem(sameInstant(DEFAULT_VALIDATE_DATE))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].tx").value(hasItem(DEFAULT_TX.toString())))
            .andExpect(jsonPath("$.[*].receiverAddress").value(hasItem(DEFAULT_RECEIVER_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getCurrencyOperation() throws Exception {
        // Initialize the database
        currencyOperationRepository.saveAndFlush(currencyOperation);

        // Get the currencyOperation
        restCurrencyOperationMockMvc.perform(get("/api/currency-operations/{id}", currencyOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currencyOperation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.validateDate").value(sameInstant(DEFAULT_VALIDATE_DATE)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.isValid").value(DEFAULT_IS_VALID.booleanValue()))
            .andExpect(jsonPath("$.tx").value(DEFAULT_TX.toString()))
            .andExpect(jsonPath("$.receiverAddress").value(DEFAULT_RECEIVER_ADDRESS.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrencyOperation() throws Exception {
        // Get the currencyOperation
        restCurrencyOperationMockMvc.perform(get("/api/currency-operations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrencyOperation() throws Exception {
        // Initialize the database
        currencyOperationRepository.saveAndFlush(currencyOperation);

        int databaseSizeBeforeUpdate = currencyOperationRepository.findAll().size();

        // Update the currencyOperation
        CurrencyOperation updatedCurrencyOperation = currencyOperationRepository.findById(currencyOperation.getId()).get();
        // Disconnect from session so that the updates on updatedCurrencyOperation are not directly saved in db
        em.detach(updatedCurrencyOperation);
        updatedCurrencyOperation
            .type(UPDATED_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .validateDate(UPDATED_VALIDATE_DATE)
            .message(UPDATED_MESSAGE)
            .isValid(UPDATED_IS_VALID)
            .tx(UPDATED_TX)
            .receiverAddress(UPDATED_RECEIVER_ADDRESS)
            .value(UPDATED_VALUE);
        CurrencyOperationDTO currencyOperationDTO = currencyOperationMapper.toDto(updatedCurrencyOperation);

        restCurrencyOperationMockMvc.perform(put("/api/currency-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyOperationDTO)))
            .andExpect(status().isOk());

        // Validate the CurrencyOperation in the database
        List<CurrencyOperation> currencyOperationList = currencyOperationRepository.findAll();
        assertThat(currencyOperationList).hasSize(databaseSizeBeforeUpdate);
        CurrencyOperation testCurrencyOperation = currencyOperationList.get(currencyOperationList.size() - 1);
        assertThat(testCurrencyOperation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCurrencyOperation.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCurrencyOperation.getValidateDate()).isEqualTo(UPDATED_VALIDATE_DATE);
        assertThat(testCurrencyOperation.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCurrencyOperation.isIsValid()).isEqualTo(UPDATED_IS_VALID);
        assertThat(testCurrencyOperation.getTx()).isEqualTo(UPDATED_TX);
        assertThat(testCurrencyOperation.getReceiverAddress()).isEqualTo(UPDATED_RECEIVER_ADDRESS);
        assertThat(testCurrencyOperation.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrencyOperation() throws Exception {
        int databaseSizeBeforeUpdate = currencyOperationRepository.findAll().size();

        // Create the CurrencyOperation
        CurrencyOperationDTO currencyOperationDTO = currencyOperationMapper.toDto(currencyOperation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyOperationMockMvc.perform(put("/api/currency-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyOperationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyOperation in the database
        List<CurrencyOperation> currencyOperationList = currencyOperationRepository.findAll();
        assertThat(currencyOperationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCurrencyOperation() throws Exception {
        // Initialize the database
        currencyOperationRepository.saveAndFlush(currencyOperation);

        int databaseSizeBeforeDelete = currencyOperationRepository.findAll().size();

        // Get the currencyOperation
        restCurrencyOperationMockMvc.perform(delete("/api/currency-operations/{id}", currencyOperation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CurrencyOperation> currencyOperationList = currencyOperationRepository.findAll();
        assertThat(currencyOperationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyOperation.class);
        CurrencyOperation currencyOperation1 = new CurrencyOperation();
        currencyOperation1.setId(1L);
        CurrencyOperation currencyOperation2 = new CurrencyOperation();
        currencyOperation2.setId(currencyOperation1.getId());
        assertThat(currencyOperation1).isEqualTo(currencyOperation2);
        currencyOperation2.setId(2L);
        assertThat(currencyOperation1).isNotEqualTo(currencyOperation2);
        currencyOperation1.setId(null);
        assertThat(currencyOperation1).isNotEqualTo(currencyOperation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyOperationDTO.class);
        CurrencyOperationDTO currencyOperationDTO1 = new CurrencyOperationDTO();
        currencyOperationDTO1.setId(1L);
        CurrencyOperationDTO currencyOperationDTO2 = new CurrencyOperationDTO();
        assertThat(currencyOperationDTO1).isNotEqualTo(currencyOperationDTO2);
        currencyOperationDTO2.setId(currencyOperationDTO1.getId());
        assertThat(currencyOperationDTO1).isEqualTo(currencyOperationDTO2);
        currencyOperationDTO2.setId(2L);
        assertThat(currencyOperationDTO1).isNotEqualTo(currencyOperationDTO2);
        currencyOperationDTO1.setId(null);
        assertThat(currencyOperationDTO1).isNotEqualTo(currencyOperationDTO2);
    }*/
/*
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(currencyOperationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(currencyOperationMapper.fromId(null)).isNull();
    }*/
}
