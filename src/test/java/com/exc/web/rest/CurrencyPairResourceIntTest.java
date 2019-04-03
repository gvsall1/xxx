package com.exc.web.rest;

import com.exc.OrderApp;

import com.exc.config.SecurityBeanOverrideConfiguration;

import com.exc.domain.CurrencyPair;
import com.exc.repository.CurrencyPairRepository;
import com.exc.service.CurrencyPairService;
import com.exc.service.dto.CurrencyPairDTO;
import com.exc.service.mapper.CurrencyPairMapper;
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
import java.util.List;


import static com.exc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CurrencyPairResource REST controller.
 *
 * @see CurrencyPairResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, OrderApp.class})
public class CurrencyPairResourceIntTest {

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @Autowired
    private CurrencyPairMapper currencyPairMapper;
    
    @Autowired
    private CurrencyPairService currencyPairService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCurrencyPairMockMvc;

    private CurrencyPair currencyPair;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyPairResource currencyPairResource = new CurrencyPairResource(currencyPairService);
        this.restCurrencyPairMockMvc = MockMvcBuilders.standaloneSetup(currencyPairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrencyPair createEntity(EntityManager em) {
        CurrencyPair currencyPair = new CurrencyPair();
        return currencyPair;
    }

    @Before
    public void initTest() {
        currencyPair = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrencyPair() throws Exception {
        int databaseSizeBeforeCreate = currencyPairRepository.findAll().size();

        // Create the CurrencyPair
        CurrencyPairDTO currencyPairDTO = currencyPairMapper.toDto(currencyPair);
        restCurrencyPairMockMvc.perform(post("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPairDTO)))
            .andExpect(status().isCreated());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeCreate + 1);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
    }

    @Test
    @Transactional
    public void createCurrencyPairWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyPairRepository.findAll().size();

        // Create the CurrencyPair with an existing ID
        currencyPair.setId(1L);
        CurrencyPairDTO currencyPairDTO = currencyPairMapper.toDto(currencyPair);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyPairMockMvc.perform(post("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCurrencyPairs() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        // Get all the currencyPairList
        restCurrencyPairMockMvc.perform(get("/api/currency-pairs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencyPair.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        // Get the currencyPair
        restCurrencyPairMockMvc.perform(get("/api/currency-pairs/{id}", currencyPair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currencyPair.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrencyPair() throws Exception {
        // Get the currencyPair
        restCurrencyPairMockMvc.perform(get("/api/currency-pairs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().size();

        // Update the currencyPair
        CurrencyPair updatedCurrencyPair = currencyPairRepository.findById(currencyPair.getId()).get();
        // Disconnect from session so that the updates on updatedCurrencyPair are not directly saved in db
        em.detach(updatedCurrencyPair);
        CurrencyPairDTO currencyPairDTO = currencyPairMapper.toDto(updatedCurrencyPair);

        restCurrencyPairMockMvc.perform(put("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPairDTO)))
            .andExpect(status().isOk());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
        CurrencyPair testCurrencyPair = currencyPairList.get(currencyPairList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrencyPair() throws Exception {
        int databaseSizeBeforeUpdate = currencyPairRepository.findAll().size();

        // Create the CurrencyPair
        CurrencyPairDTO currencyPairDTO = currencyPairMapper.toDto(currencyPair);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyPairMockMvc.perform(put("/api/currency-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyPairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CurrencyPair in the database
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCurrencyPair() throws Exception {
        // Initialize the database
        currencyPairRepository.saveAndFlush(currencyPair);

        int databaseSizeBeforeDelete = currencyPairRepository.findAll().size();

        // Get the currencyPair
        restCurrencyPairMockMvc.perform(delete("/api/currency-pairs/{id}", currencyPair.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        assertThat(currencyPairList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyPair.class);
        CurrencyPair currencyPair1 = new CurrencyPair();
        currencyPair1.setId(1L);
        CurrencyPair currencyPair2 = new CurrencyPair();
        currencyPair2.setId(currencyPair1.getId());
        assertThat(currencyPair1).isEqualTo(currencyPair2);
        currencyPair2.setId(2L);
        assertThat(currencyPair1).isNotEqualTo(currencyPair2);
        currencyPair1.setId(null);
        assertThat(currencyPair1).isNotEqualTo(currencyPair2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyPairDTO.class);
        CurrencyPairDTO currencyPairDTO1 = new CurrencyPairDTO();
        currencyPairDTO1.setId(1L);
        CurrencyPairDTO currencyPairDTO2 = new CurrencyPairDTO();
        assertThat(currencyPairDTO1).isNotEqualTo(currencyPairDTO2);
        currencyPairDTO2.setId(currencyPairDTO1.getId());
        assertThat(currencyPairDTO1).isEqualTo(currencyPairDTO2);
        currencyPairDTO2.setId(2L);
        assertThat(currencyPairDTO1).isNotEqualTo(currencyPairDTO2);
        currencyPairDTO1.setId(null);
        assertThat(currencyPairDTO1).isNotEqualTo(currencyPairDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(currencyPairMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(currencyPairMapper.fromId(null)).isNull();
    }
}
