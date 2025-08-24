package com.acc.chatdemo.redis;

import com.acc.chatdemo.redis.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CHANNEL_NAME = "chat_messages";

    public void publishMessage(MessageDto message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            
            redisTemplate.convertAndSend(CHANNEL_NAME, jsonMessage);
        } catch (JsonProcessingException e) {
            System.err.println("메시지 직렬화 오류: " + e.getMessage());
            throw new RuntimeException("메시지 직렬화 오류", e);
        }
    }
}
