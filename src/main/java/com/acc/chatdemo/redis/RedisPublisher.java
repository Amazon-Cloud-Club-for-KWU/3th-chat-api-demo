package com.acc.chatdemo.redis;

import com.acc.chatdemo.redis.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CHANNEL_NAME = "chat_messages";

    public void publishMessage(MessageDto message) {
        redisTemplate.convertAndSend(CHANNEL_NAME, message);
    }
}
