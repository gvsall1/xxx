package com.exc.service;

import com.exc.OrderApp;
import com.exc.repository.CryptoCurrencyRepository;

import com.exc.service.mapper.order.OrderPairMapperFactory;
import com.exc.service.remote.TxService;
import com.exc.service.remote.UserService;
import io.github.jhipster.config.JHipsterConstants;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApp.class)
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
@ActiveProfiles({JHipsterConstants.SPRING_PROFILE_TEST})
public abstract class AbstractServiceTest {

    @Autowired
    protected OrderPairMapperFactory orderPairMapperFactory;

    @Autowired
    protected CryptoCurrencyRepository cryptoCurrencyRepository;
    @MockBean
    protected TxService txService;
    @MockBean
    protected UserService userService;


 /*   @Before
    public void setup() {
        INode node = new LocalNode();
        when(nodeAdapterFactory.getNode(anyString())).thenReturn(node);
        //create profile for all users
        userRepository.findAll().forEach(user -> {
            userProfileService.getProfile(user.getLogin());
        });
        userInfo = userInfoRepository.findOneByUserLogin("user").get();
        userInfo2 = userInfoRepository.findOneByUserLogin("user2").get();
        Hibernate.initialize(userInfo);

        wallet1 = walletRepository.findByUserInfo_Id(userInfo.getId()).get(0);
    }*/

}
