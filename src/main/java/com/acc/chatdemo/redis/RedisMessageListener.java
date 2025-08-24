package com.acc.chatdemo.redis;

import com.acc.chatdemo.chat.dto.ChatMessageDto;
import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.chat.repository.ChatMessageRepository;
import com.acc.chatdemo.redis.dto.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
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
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate
                    .getStringSerializer().deserialize(message.getBody());

            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);
            ChatMessage chatMessage = chatMessageRepository.findById(messageDto.getMessageId())
                    .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

            messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), ChatMessageDto.toDto(chatMessage));
        } catch (JsonProcessingException e) {

        }
    }
}
