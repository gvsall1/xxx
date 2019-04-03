package com.exc.service.sync;

import com.exc.config.redis.RedisConfiguration;
import com.exc.service.errors.BalanceLockException;
import com.exc.service.errors.OrderLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Responsible for handling orders concurrency. TODO: sync in cluster
 */
@Component
public class OrderSyncService {
    private final Logger log = LoggerFactory.getLogger(OrderSyncService.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public OrderSyncService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param id
     * @param pairId
     * @return true in case lock success
     */
    public boolean tryLock(Long id, Long pairId) throws InterruptedException {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String lockKEy = genKey(id, pairId);
        String wKey = genKey(id, pairId);
        int lockCount = 0;
        Boolean isLock = values.setIfAbsent(lockKEy, Boolean.TRUE);

        while (!isLock) {
            isLock = values.setIfAbsent(lockKEy, Boolean.TRUE);
            if (!isLock) {
                Thread.sleep(100l);
            }
            lockCount++;
            if (lockCount > 20) {
                log.error("Failed to lock order/pair Id {}/{} after 20 attempts", id, pairId);
                throw new OrderLockException(String.format("Can't lock order!"));
            }
        }


        return isLock ;
    }


    public void unlockIds(Long pairId, List<Long> orderPairs) {
        for (Long op : orderPairs) {
            //todo: in pipe
            redisTemplate.delete(genKey(op, pairId));

        }
    }

    private String genKey(Long id, Long pairId) {
        StringBuilder builder = new StringBuilder();
        builder.append( RedisConfiguration.ORDERS_SYNC_LOCK_KEY );
        builder.append(id);
        builder.append('-');
        builder.append(pairId);

        return builder.toString();
    }


}
