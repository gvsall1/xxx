package com.exc.service;

import com.exc.config.Constants;
import com.exc.service.dto.OrderPairDTO;
import com.exc.service.dto.QuoteDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClientWebSocketService {
    private final RedisTemplate redisTemplate;
    String Q_SUFFIX = "qu-";

    public ClientWebSocketService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sendPersonalOrderNotification(String userName, OrderPairDTO orderPairDTO) {
    }

    public void sendQuote(QuoteDTO quoteDTO) {
        redisTemplate.convertAndSend(Q_SUFFIX + quoteDTO.getBuy() + quoteDTO.getSell(), quoteDTO);
       // messagingTempQuoteWSDTOlate.convertAndSend("/topic/trade/" + pairId, quoteWSDTO);
    }

}
