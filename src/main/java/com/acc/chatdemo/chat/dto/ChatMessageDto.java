package com.acc.chatdemo.chat.dto;

import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {
    private String id;
    private String content;
    private LocalDateTime createdAt;
    private Long seq;
    private UserDto sender;

    public static ChatMessageDto toDto(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .seq(chatMessage.getSeq())
                .createdAt(chatMessage.getCreatedAt())
                .sender(UserDto.toDto(chatMessage.getSender()))
                .build();
    }
}
