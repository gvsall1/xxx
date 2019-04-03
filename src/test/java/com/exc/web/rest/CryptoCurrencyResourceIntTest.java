package com.exc.web.rest;

import com.exc.OrderApp;

import com.exc.config.SecurityBeanOverrideConfiguration;

import com.exc.domain.CryptoCurrency;
import com.exc.repository.CryptoCurrencyRepository;
import com.exc.service.CryptoCurrencyService;
import com.exc.service.dto.CryptoCurrencyDTO;
import com.exc.service.mapper.CryptoCurrencyMapper;
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
 * Test class for the CryptoCurrencyResource REST controller.
 *
 * @see CryptoCurrencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, OrderApp.class})
public class CryptoCurrencyResourceIntTest {
/*
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RATE_URL = "AAAAAAAAAA";
    private static final String UPDATED_RATE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_TOKEN = false;
    private static final Boolean UPDATED_IS_TOKEN = true;

    private static final String DEFAULT_NODE_URL_SEND_TX = "AAAAAAAAAA";
    private static final String UPDATED_NODE_URL_SEND_TX = "BBBBBBBBBB";

    private static final String DEFAULT_NODE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_NODE_STATUS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MIN_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MIN_AMOUNT = new BigDecimal(2);

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private CryptoCurrencyMapper cryptoCurrencyMapper;
    
    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCryptoCurrencyMockMvc;

    private CryptoCurrency cryptoCurrency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CryptoCurrencyResource cryptoCurrencyResource = new CryptoCurrencyResource(cryptoCurrencyService);
        this.restCryptoCurrencyMockMvc = MockMvcBuilders.standaloneSetup(cryptoCurrencyResource)
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
    public static CryptoCurrency createEntity(EntityManager em) {
        CryptoCurrency cryptoCurrency = new CryptoCurrency()
            .name(DEFAULT_NAME)
            .rateUrl(DEFAULT_RATE_URL)
            .isToken(DEFAULT_IS_TOKEN)
            .nodeUrlSendTX(DEFAULT_NODE_URL_SEND_TX)
            .nodeStatus(DEFAULT_NODE_STATUS)
            .minAmount(DEFAULT_MIN_AMOUNT);
        return cryptoCurrency;
    }

    @Before
    public void initTest() {
        cryptoCurrency = createEntity(em);
    }

    @Test
    @Transactional
    public void createCryptoCurrency() throws Exception {
        int databaseSizeBeforeCreate = cryptoCurrencyRepository.findAll().size();

        // Create the CryptoCurrency
        CryptoCurrencyDTO cryptoCurrencyDTO = cryptoCurrencyMapper.toDto(cryptoCurrency);
        restCryptoCurrencyMockMvc.perform(post("/api/crypto-currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptoCurrencyDTO)))
            .andExpect(status().isCreated());

        // Validate the CryptoCurrency in the database
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        assertThat(cryptoCurrencyList).hasSize(databaseSizeBeforeCreate + 1);
        CryptoCurrency testCryptoCurrency = cryptoCurrencyList.get(cryptoCurrencyList.size() - 1);
        assertThat(testCryptoCurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCryptoCurrency.getRateUrl()).isEqualTo(DEFAULT_RATE_URL);
        assertThat(testCryptoCurrency.isIsToken()).isEqualTo(DEFAULT_IS_TOKEN);
        assertThat(testCryptoCurrency.getNodeUrlSendTX()).isEqualTo(DEFAULT_NODE_URL_SEND_TX);
        assertThat(testCryptoCurrency.getNodeStatus()).isEqualTo(DEFAULT_NODE_STATUS);
        assertThat(testCryptoCurrency.getMinAmount()).isEqualTo(DEFAULT_MIN_AMOUNT);
    }

    @Test
    @Transactional
    public void createCryptoCurrencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cryptoCurrencyRepository.findAll().size();

        // Create the CryptoCurrency with an existing ID
        cryptoCurrency.setId(1L);
        CryptoCurrencyDTO cryptoCurrencyDTO = cryptoCurrencyMapper.toDto(cryptoCurrency);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCryptoCurrencyMockMvc.perform(post("/api/crypto-currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptoCurrencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CryptoCurrency in the database
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        assertThat(cryptoCurrencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cryptoCurrencyRepository.findAll().size();
        // set the field null
        cryptoCurrency.setName(null);

        // Create the CryptoCurrency, which fails.
        CryptoCurrencyDTO cryptoCurrencyDTO = cryptoCurrencyMapper.toDto(cryptoCurrency);

        restCryptoCurrencyMockMvc.perform(post("/api/crypto-currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptoCurrencyDTO)))
            .andExpect(status().isBadRequest());

        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        assertThat(cryptoCurrencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCryptoCurrencies() throws Exception {
        // Initialize the database
        cryptoCurrencyRepository.saveAndFlush(cryptoCurrency);

        // Get all the cryptoCurrencyList
        restCryptoCurrencyMockMvc.perform(get("/api/crypto-currencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cryptoCurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].rateUrl").value(hasItem(DEFAULT_RATE_URL.toString())))
            .andExpect(jsonPath("$.[*].isToken").value(hasItem(DEFAULT_IS_TOKEN.booleanValue())))
            .andExpect(jsonPath("$.[*].nodeUrlSendTX").value(hasItem(DEFAULT_NODE_URL_SEND_TX.toString())))
            .andExpect(jsonPath("$.[*].nodeStatus").value(hasItem(DEFAULT_NODE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getCryptoCurrency() throws Exception {
        // Initialize the database
        cryptoCurrencyRepository.saveAndFlush(cryptoCurrency);

        // Get the cryptoCurrency
        restCryptoCurrencyMockMvc.perform(get("/api/crypto-currencies/{id}", cryptoCurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cryptoCurrency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rateUrl").value(DEFAULT_RATE_URL.toString()))
            .andExpect(jsonPath("$.isToken").value(DEFAULT_IS_TOKEN.booleanValue()))
            .andExpect(jsonPath("$.nodeUrlSendTX").value(DEFAULT_NODE_URL_SEND_TX.toString()))
            .andExpect(jsonPath("$.nodeStatus").value(DEFAULT_NODE_STATUS.toString()))
            .andExpect(jsonPath("$.minAmount").value(DEFAULT_MIN_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCryptoCurrency() throws Exception {
        // Get the cryptoCurrency
        restCryptoCurrencyMockMvc.perform(get("/api/crypto-currencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCryptoCurrency() throws Exception {
        // Initialize the database
        cryptoCurrencyRepository.saveAndFlush(cryptoCurrency);

        int databaseSizeBeforeUpdate = cryptoCurrencyRepository.findAll().size();

        // Update the cryptoCurrency
        CryptoCurrency updatedCryptoCurrency = cryptoCurrencyRepository.findById(cryptoCurrency.getId()).get();
        // Disconnect from session so that the updates on updatedCryptoCurrency are not directly saved in db
        em.detach(updatedCryptoCurrency);
        updatedCryptoCurrency
            .name(UPDATED_NAME)
            .rateUrl(UPDATED_RATE_URL)
            .isToken(UPDATED_IS_TOKEN)
            .nodeUrlSendTX(UPDATED_NODE_URL_SEND_TX)
            .nodeStatus(UPDATED_NODE_STATUS)
            .minAmount(UPDATED_MIN_AMOUNT);
        CryptoCurrencyDTO cryptoCurrencyDTO = cryptoCurrencyMapper.toDto(updatedCryptoCurrency);

        restCryptoCurrencyMockMvc.perform(put("/api/crypto-currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptoCurrencyDTO)))
            .andExpect(status().isOk());

        // Validate the CryptoCurrency in the database
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        assertThat(cryptoCurrencyList).hasSize(databaseSizeBeforeUpdate);
        CryptoCurrency testCryptoCurrency = cryptoCurrencyList.get(cryptoCurrencyList.size() - 1);
        assertThat(testCryptoCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCryptoCurrency.getRateUrl()).isEqualTo(UPDATED_RATE_URL);
        assertThat(testCryptoCurrency.isIsToken()).isEqualTo(UPDATED_IS_TOKEN);
        assertThat(testCryptoCurrency.getNodeUrlSendTX()).isEqualTo(UPDATED_NODE_URL_SEND_TX);
        assertThat(testCryptoCurrency.getNodeStatus()).isEqualTo(UPDATED_NODE_STATUS);
        assertThat(testCryptoCurrency.getMinAmount()).isEqualTo(UPDATED_MIN_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCryptoCurrency() throws Exception {
        int databaseSizeBeforeUpdate = cryptoCurrencyRepository.findAll().size();

        // Create the CryptoCurrency
        CryptoCurrencyDTO cryptoCurrencyDTO = cryptoCurrencyMapper.toDto(cryptoCurrency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCryptoCurrencyMockMvc.perform(put("/api/crypto-currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cryptoCurrencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CryptoCurrency in the database
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        assertThat(cryptoCurrencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCryptoCurrency() throws Exception {
        // Initialize the database
        cryptoCurrencyRepository.saveAndFlush(cryptoCurrency);

        int databaseSizeBeforeDelete = cryptoCurrencyRepository.findAll().size();

        // Get the cryptoCurrency
        restCryptoCurrencyMockMvc.perform(delete("/api/crypto-currencies/{id}", cryptoCurrency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        assertThat(cryptoCurrencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CryptoCurrency.class);
        CryptoCurrency cryptoCurrency1 = new CryptoCurrency();
        cryptoCurrency1.setId(1L);
        CryptoCurrency cryptoCurrency2 = new CryptoCurrency();
        cryptoCurrency2.setId(cryptoCurrency1.getId());
        assertThat(cryptoCurrency1).isEqualTo(cryptoCurrency2);
        cryptoCurrency2.setId(2L);
        assertThat(cryptoCurrency1).isNotEqualTo(cryptoCurrency2);
        cryptoCurrency1.setId(null);
        assertThat(cryptoCurrency1).isNotEqualTo(cryptoCurrency2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CryptoCurrencyDTO.class);
        CryptoCurrencyDTO cryptoCurrencyDTO1 = new CryptoCurrencyDTO();
        cryptoCurrencyDTO1.setId(1L);
        CryptoCurrencyDTO cryptoCurrencyDTO2 = new CryptoCurrencyDTO();
        assertThat(cryptoCurrencyDTO1).isNotEqualTo(cryptoCurrencyDTO2);
        cryptoCurrencyDTO2.setId(cryptoCurrencyDTO1.getId());
        assertThat(cryptoCurrencyDTO1).isEqualTo(cryptoCurrencyDTO2);
        cryptoCurrencyDTO2.setId(2L);
        assertThat(cryptoCurrencyDTO1).isNotEqualTo(cryptoCurrencyDTO2);
        cryptoCurrencyDTO1.setId(null);
        assertThat(cryptoCurrencyDTO1).isNotEqualTo(cryptoCurrencyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cryptoCurrencyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cryptoCurrencyMapper.fromId(null)).isNull();
    }*/
}
