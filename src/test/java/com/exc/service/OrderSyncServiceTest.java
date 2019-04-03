package com.exc.service;


import com.exc.service.errors.OrderLockException;
import com.exc.service.sync.OrderSyncService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class OrderSyncServiceTest extends AbstractServiceTest {
    @Autowired
    private OrderSyncService orderSyncService;
    @Mock
    private ValueOperations valueOperations;

    @MockBean
    RedisTemplate<String, Object> redisTemplate;

    @Before
    public void init() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(true);
        when(redisTemplate.delete(anyString())).thenReturn(true);
    }

    @Test
    public void testLocalSync() throws InterruptedException {
        boolean res = orderSyncService.tryLock(1l, 1l);
        assertThat(res).isTrue();
        orderSyncService.unlockIds(1l, Arrays.asList(1l));
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(null);
        res = orderSyncService.tryLock(1l, 1l);
        assertThat(res).isTrue();

    }

    @Test(expected = OrderLockException.class)
    public void testLockErr() throws InterruptedException {
        when(valueOperations.setIfAbsent(any(), any())).thenReturn(false);
        orderSyncService.tryLock(1l, 1l);
    }
}
