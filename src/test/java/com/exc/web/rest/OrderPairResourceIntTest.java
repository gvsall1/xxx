package com.exc.web.rest;

import com.exc.OrderApp;
import com.exc.config.SecurityBeanOverrideConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
 * Test class for the OrderPairResource REST controller.
 *
 * @see OrderPairResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, OrderApp.class})
public class OrderPairResourceIntTest {
/*
    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CANCEL_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CANCEL_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXECUTED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXECUTED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);

    private static final OrderType DEFAULT_TYPE = OrderType.BUY;
    private static final OrderType UPDATED_TYPE = OrderType.SELL;

    private static final OrderStatusType DEFAULT_STATUS = OrderStatusType.OPEN;
    private static final OrderStatusType UPDATED_STATUS = OrderStatusType.IN_PROCESS;

    @Autowired
    private OrderPairRepository orderPairRepository;

    @Mock
    private OrderPairRepository orderPairRepositoryMock;

    @Autowired
    private OrderPairMapper orderPairMapper;
    

    @Mock
    private OrderPairService orderPairServiceMock;

    @Autowired
    private OrderPairService orderPairService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderPairMockMvc;

    private OrderPair orderPair;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderPairResource orderPairResource = new OrderPairResource(orderPairService);
        this.restOrderPairMockMvc = MockMvcBuilders.standaloneSetup(orderPairResource)
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
    public static OrderPair createEntity(EntityManager em) {
        OrderPair orderPair = new OrderPair()
            .createDate(DEFAULT_CREATE_DATE)
            .cancelDate(DEFAULT_CANCEL_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE)
            .executedDate(DEFAULT_EXECUTED_DATE)
            .value(DEFAULT_VALUE)
            .rate(DEFAULT_RATE)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return orderPair;
    }

    @Before
    public void initTest() {
        orderPair = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderPair() throws Exception {
        int databaseSizeBeforeCreate = orderPairRepository.findAll().size();

        // Create the OrderPair
        OrderPairDTO orderPairDTO = orderPairMapper.toDto(orderPair);
        restOrderPairMockMvc.perform(post("/api/order-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderPair in the database
        List<OrderPair> orderPairList = orderPairRepository.findAll();
        assertThat(orderPairList).hasSize(databaseSizeBeforeCreate + 1);
        OrderPair testOrderPair = orderPairList.get(orderPairList.size() - 1);
        assertThat(testOrderPair.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testOrderPair.getCancelDate()).isEqualTo(DEFAULT_CANCEL_DATE);
        assertThat(testOrderPair.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testOrderPair.getExecutedDate()).isEqualTo(DEFAULT_EXECUTED_DATE);
        assertThat(testOrderPair.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testOrderPair.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testOrderPair.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrderPair.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOrderPairWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderPairRepository.findAll().size();

        // Create the OrderPair with an existing ID
        orderPair.setId(1L);
        OrderPairDTO orderPairDTO = orderPairMapper.toDto(orderPair);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderPairMockMvc.perform(post("/api/order-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPair in the database
        List<OrderPair> orderPairList = orderPairRepository.findAll();
        assertThat(orderPairList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrderPairs() throws Exception {
        // Initialize the database
        orderPairRepository.saveAndFlush(orderPair);

        // Get all the orderPairList
        restOrderPairMockMvc.perform(get("/api/order-pairs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderPair.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].cancelDate").value(hasItem(sameInstant(DEFAULT_CANCEL_DATE))))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(sameInstant(DEFAULT_MODIFY_DATE))))
            .andExpect(jsonPath("$.[*].executedDate").value(hasItem(sameInstant(DEFAULT_EXECUTED_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    public void getAllOrderPairsWithEagerRelationshipsIsEnabled() throws Exception {
        OrderPairResource orderPairResource = new OrderPairResource(orderPairServiceMock);
        when(orderPairServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOrderPairMockMvc = MockMvcBuilders.standaloneSetup(orderPairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrderPairMockMvc.perform(get("/api/order-pairs?eagerload=true"))
        .andExpect(status().isOk());

        verify(orderPairServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllOrderPairsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OrderPairResource orderPairResource = new OrderPairResource(orderPairServiceMock);
            when(orderPairServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOrderPairMockMvc = MockMvcBuilders.standaloneSetup(orderPairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOrderPairMockMvc.perform(get("/api/order-pairs?eagerload=true"))
        .andExpect(status().isOk());

            verify(orderPairServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrderPair() throws Exception {
        // Initialize the database
        orderPairRepository.saveAndFlush(orderPair);

        // Get the orderPair
        restOrderPairMockMvc.perform(get("/api/order-pairs/{id}", orderPair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderPair.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.cancelDate").value(sameInstant(DEFAULT_CANCEL_DATE)))
            .andExpect(jsonPath("$.modifyDate").value(sameInstant(DEFAULT_MODIFY_DATE)))
            .andExpect(jsonPath("$.executedDate").value(sameInstant(DEFAULT_EXECUTED_DATE)))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderPair() throws Exception {
        // Get the orderPair
        restOrderPairMockMvc.perform(get("/api/order-pairs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderPair() throws Exception {
        // Initialize the database
        orderPairRepository.saveAndFlush(orderPair);

        int databaseSizeBeforeUpdate = orderPairRepository.findAll().size();

        // Update the orderPair
        OrderPair updatedOrderPair = orderPairRepository.findById(orderPair.getId()).get();
        // Disconnect from session so that the updates on updatedOrderPair are not directly saved in db
        em.detach(updatedOrderPair);
        updatedOrderPair
            .createDate(UPDATED_CREATE_DATE)
            .cancelDate(UPDATED_CANCEL_DATE)
            .modifyDate(UPDATED_MODIFY_DATE)
            .executedDate(UPDATED_EXECUTED_DATE)
            .value(UPDATED_VALUE)
            .rate(UPDATED_RATE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        OrderPairDTO orderPairDTO = orderPairMapper.toDto(updatedOrderPair);

        restOrderPairMockMvc.perform(put("/api/order-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairDTO)))
            .andExpect(status().isOk());

        // Validate the OrderPair in the database
        List<OrderPair> orderPairList = orderPairRepository.findAll();
        assertThat(orderPairList).hasSize(databaseSizeBeforeUpdate);
        OrderPair testOrderPair = orderPairList.get(orderPairList.size() - 1);
        assertThat(testOrderPair.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testOrderPair.getCancelDate()).isEqualTo(UPDATED_CANCEL_DATE);
        assertThat(testOrderPair.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testOrderPair.getExecutedDate()).isEqualTo(UPDATED_EXECUTED_DATE);
        assertThat(testOrderPair.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testOrderPair.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testOrderPair.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrderPair.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderPair() throws Exception {
        int databaseSizeBeforeUpdate = orderPairRepository.findAll().size();

        // Create the OrderPair
        OrderPairDTO orderPairDTO = orderPairMapper.toDto(orderPair);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderPairMockMvc.perform(put("/api/order-pairs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPairDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPair in the database
        List<OrderPair> orderPairList = orderPairRepository.findAll();
        assertThat(orderPairList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderPair() throws Exception {
        // Initialize the database
        orderPairRepository.saveAndFlush(orderPair);

        int databaseSizeBeforeDelete = orderPairRepository.findAll().size();

        // Get the orderPair
        restOrderPairMockMvc.perform(delete("/api/order-pairs/{id}", orderPair.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderPair> orderPairList = orderPairRepository.findAll();
        assertThat(orderPairList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderPair.class);
        OrderPair orderPair1 = new OrderPair();
        orderPair1.setId(1L);
        OrderPair orderPair2 = new OrderPair();
        orderPair2.setId(orderPair1.getId());
        assertThat(orderPair1).isEqualTo(orderPair2);
        orderPair2.setId(2L);
        assertThat(orderPair1).isNotEqualTo(orderPair2);
        orderPair1.setId(null);
        assertThat(orderPair1).isNotEqualTo(orderPair2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderPairDTO.class);
        OrderPairDTO orderPairDTO1 = new OrderPairDTO();
        orderPairDTO1.setId(1L);
        OrderPairDTO orderPairDTO2 = new OrderPairDTO();
        assertThat(orderPairDTO1).isNotEqualTo(orderPairDTO2);
        orderPairDTO2.setId(orderPairDTO1.getId());
        assertThat(orderPairDTO1).isEqualTo(orderPairDTO2);
        orderPairDTO2.setId(2L);
        assertThat(orderPairDTO1).isNotEqualTo(orderPairDTO2);
        orderPairDTO1.setId(null);
        assertThat(orderPairDTO1).isNotEqualTo(orderPairDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(orderPairMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(orderPairMapper.fromId(null)).isNull();
    }*/
}
