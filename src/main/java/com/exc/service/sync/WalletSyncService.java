package com.exc.service.sync;

import com.exc.config.redis.RedisConfiguration;
import com.exc.domain.CurrencyName;
import com.exc.service.errors.BalanceLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class WalletSyncService {
    private final Logger log = LoggerFactory.getLogger(WalletSyncService.class);
    private final RedisTemplate<String, Object> redisTemplate;


    public WalletSyncService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Will try lock wallet between services in cluster.
     *
     * @param userId
     * @param curId
     * @return current lock balance
     * @throws InterruptedException
     */
    public BigInteger tryLock(Long userId, CurrencyName curId) throws InterruptedException {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String lockKEy = getKey(userId, curId, true);
        String wKey = getKey(userId, curId, false);
        int lockCount = 0;
        Boolean isLock = values.setIfAbsent(lockKEy, Boolean.TRUE);

        while (!isLock) {
            isLock = values.setIfAbsent(lockKEy, Boolean.TRUE);
            if (!isLock) {
                Thread.sleep(100l);
            }
            lockCount++;
            if (lockCount > 20) {
                log.error("Failed to lock  Id {} after 20 attempts", userId);
                throw new BalanceLockException(String.format("Can't lock wallet!"));
            }
        }

        byte[] digit = (byte[]) values.get(wKey);
        if (digit == null) {
            digit = new byte[1];
            digit[0] = 0;
        }
        return new BigInteger(digit);

    }

    /**
     * will unlock wallet between services in cluster.
     *
     * @param userId
     * @param curId
     * @param newBalance
     */
    public void unLock(Long userId, CurrencyName curId, BigInteger newBalance) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String lockKEy = getKey(userId, curId, true);
        String wKey = getKey(userId, curId, false);
        values.set(wKey, newBalance);
        redisTemplate.delete(lockKEy);

    }

    public BigInteger getLockedBalance(Long userId, CurrencyName curId) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String wKey = getKey(userId, curId, false);
        byte[] digit = (byte[]) values.get(wKey);
        if (digit == null) {
            digit = new byte[1];
            digit[0] = 0;
        }
        return new BigInteger(digit);
    }

    private String getKey(Long userId, CurrencyName curId, boolean isLock) {
        StringBuilder builder = new StringBuilder();
        if (isLock)
            builder.append(RedisConfiguration.BALANCE_SYNC_LOCK_KEY);
        else builder.append(RedisConfiguration.BALANCE_SYNC_KEY);
        builder.append(curId.name());
        builder.append('-');
        builder.append(userId);

        return builder.toString();

    }


    /**
     * will keep track of credit/deposit in RAM
     */
/*
    public boolean modBalance(Long curId, Long userId, BigInteger lockSum, boolean isCredit) throws InterruptedException {
        boolean success = false;
        boolean wasErr = false;
        CryptoCurrency cryptoCurrency = cryptoCurrencyRepository.getOne(curId);

        BigInteger feeDep = cryptoCurrency.getFee().getDeposit(), feeWith = cryptoCurrency.getFee().getWithdraw();
        BigInteger fee = feeDep.compareTo(feeWith) > 0 ? feeDep : feeWith;
        BalanceStatusDTO dto = txService.getBalanceStatus(cryptoCurrency.getCurrencyName());
        BigInteger currentBalance = dto.getBalance();
        BigInteger lockBalance = tryLock(userId, cryptoCurrency.getCurrencyName());
        BigInteger newBalance = lockBalance;
        try {
            if (isCredit && lockSum.compareTo(currentBalance.add(fee).add(dto.getNetFee())) >= 0) {
                wasErr = true;
                log.warn("Balance lock failed userId-{} {}", userId, cryptoCurrency.getCurrencyName().name());
            }*//* else if (!isCredit && sync.lockSum.compareTo(lockSum) < 0) {
                wasErr = true;
                log.error("Balance UnLock failed wallet {}, lockSum {}", walletId, lockSum.toString());
            }*//*

            if (!wasErr) {
                if (isCredit) {
                    newBalance = lockBalance.add(lockSum);
                } else {
                    newBalance = lockBalance.subtract(lockSum);
                }
                success = true;

            }
        } finally {
            unLock(userId, cryptoCurrency.getCurrencyName(), newBalance);
        }

        return success;
    }*/

}
