package com.acc.chatdemo.redis;

import com.acc.chatdemo.redis.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageListener implements MessageListener {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate
                    .getStringSerializer().deserialize(message.getBody());

            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
        } catch (JsonProcessingException e) {

        }
    }
}
