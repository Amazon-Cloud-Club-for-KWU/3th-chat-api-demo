package com.acc.chatdemo.chat.event;

import com.acc.chatdemo.redis.RedisPublisher;
import com.acc.chatdemo.redis.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class ChatMessageEventListener {
    private final RedisPublisher redisPublisher;
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatMessageCreated(ChatMessageCreatedEvent event) {
        
        MessageDto messageDto = MessageDto.builder()
                .messageId(event.getMessageId())
                .roomId(event.getRoomId().toString())
                .sender(event.getSenderId().toString())
                .timestamp(event.getTimestamp())
                .build();
        
        redisPublisher.publishMessage(messageDto);
    }
}