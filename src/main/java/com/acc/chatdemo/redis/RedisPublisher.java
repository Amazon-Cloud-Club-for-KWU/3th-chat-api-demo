package com.acc.chatdemo.redis;

import com.acc.chatdemo.redis.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.bindings.stomp.annotations.StompAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CHANNEL_NAME = "chat_messages";
    @AsyncPublisher(
            operation =
            @AsyncOperation(
                    channelName = "sub/" + CHANNEL_NAME,
                    description = "Custom channel for publishing chat messages"
            )
    )
    @StompAsyncOperationBinding
    public void publishMessage(MessageDto message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            
            redisTemplate.convertAndSend(CHANNEL_NAME, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("메시지 직렬화 오류: {}", e.getMessage());
            throw new RuntimeException("메시지 직렬화 오류", e);
        }
    }
}
