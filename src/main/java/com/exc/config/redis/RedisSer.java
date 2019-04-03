package com.exc.config.redis;

import com.exc.config.KryoContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class RedisSer implements RedisSerializer<Object> {
    private KryoContext kryoContext;

    public RedisSer(KryoContext kryoContext) {
        this.kryoContext = kryoContext;
    }

    @Override
    public byte[] serialize(Object quoteDTO) throws SerializationException {
        return kryoContext.ser(quoteDTO);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return kryoContext.des(Object.class, bytes);
    }
}
