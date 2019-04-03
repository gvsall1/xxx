package com.exc.service;

import com.exc.domain.CryptoCurrency;
import com.exc.domain.CurrencyName;
import com.exc.service.dto.CryptoCurrencyDTO;
import com.exc.service.errors.BalanceLockException;
import com.exc.service.sync.WalletSyncService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class WalletSyncServiceTest extends AbstractServiceTest {
    @Autowired
    WalletSyncService walletSyncService;
    @Autowired
    CryptoCurrencyService cryptoCurrencyService;

    @Mock
    private ValueOperations valueOperations;

    @MockBean
    RedisTemplate<String, Object> redisTemplate;

    Long w1 = 1l;
    CurrencyName c1 = CurrencyName.ETH;

    @Before
    public void init() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(null);
        doNothing().when(redisTemplate).delete(anyString());
    }

    @Test(expected = BalanceLockException.class)
    public void checkLockDead() throws InterruptedException {
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(false);
        walletSyncService.tryLock(w1, c1);
    }

    @Test
    public void checkLockOk() throws InterruptedException {
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(true);
        assertThat(walletSyncService.tryLock(w1, c1)).isNotNull();
    }

    @Test
    public void checkUnLockOk() {
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(true);
        walletSyncService.unLock(w1, c1, BigInteger.ONE);
    }


    @Test
    public void checkGetLock() {
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(true);
        assertThat(walletSyncService.getLockedBalance(w1, c1)).isNotNull();
    }

   /* @Test
    public void shouldCheckModBalance() throws InterruptedException {
        when(walletSyncService.tryLock(any(), any())).thenReturn(BigInteger.ONE);
        doNothing().when(walletSyncService).unLock(any(), any(), any());
        CryptoCurrencyDTO cur = cryptoCurrencyService.findAll(null).iterator().next();

        assertThat(walletSyncService.modBalance(cur.getId(), 1l, new BigInteger("1"), true)).isTrue();
        assertThat(walletSyncService.modBalance(cur.getId(), 1l, new BigInteger("1"), false)).isTrue();

    }*/

}
