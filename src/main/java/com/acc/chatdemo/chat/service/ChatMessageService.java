package com.acc.chatdemo.chat.service;

import com.acc.chatdemo.cache.CacheManager;
import com.acc.chatdemo.chat.dto.CreateChatMessageDto;
import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.chat.entity.ChatRoomMember;
import com.acc.chatdemo.chat.event.ChatMessageCreatedEvent;
import com.acc.chatdemo.chat.repository.ChatMessageRepository;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.repository.ChatRoomMemberRepository;
import com.acc.chatdemo.chat.repository.ChatRoomRepository;
import com.acc.chatdemo.user.entity.User;
import com.acc.chatdemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;

    @Transactional(readOnly = true)
    public Page<ChatMessage> getChatMessagesByChatRoomId(Long chatRoomId, int page, int size) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public ChatMessage findById(String id) {
        return chatMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat message not found with id: " + id));
    }

    @Transactional
    public ChatMessage createChatMessage(CreateChatMessageDto dto, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + dto.getChatRoomId()));
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        chatRoomMemberRepository.findByChatRoomAndUser(chatRoom, sender).orElseThrow(() -> new RuntimeException("User is not a member of the chat room with id: " + dto.getChatRoomId()));

        String lastMessageSeqCacheKey = "chatRoom:" + chatRoom.getId() + ":lastMessageSeq";

        Long lastMessageSeq = cacheManager.get(lastMessageSeqCacheKey, Long.class, () -> {
            ChatMessage lastMessage = chatRoom.getLastMessage();
            return lastMessage != null ? lastMessage.getSeq() : 0L;
        });

        ChatMessage chatMessage = ChatMessage.builder()
                .content(dto.getContent())
                .seq(lastMessageSeq + 1)
                .sender(sender)
                .chatRoom(chatRoom)
                .createdAt(LocalDateTime.now())
                .build();

        cacheManager.increment(lastMessageSeqCacheKey, 1L);
        chatRoom.setLastMessage(chatMessage);

        return chatMessageRepository.save(chatMessage);
    }

    @Transactional
    public void sendMessage(CreateChatMessageDto dto, Long userId) {

        ChatMessage chatMessage = createChatMessage(dto, userId);

        ChatMessageCreatedEvent event = new ChatMessageCreatedEvent(
                chatMessage.getId(),
                chatMessage.getChatRoom().getId(),
                userId,
                chatMessage.getCreatedAt().toString()
        );
        
        eventPublisher.publishEvent(event);
    }
}
