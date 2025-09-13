package com.acc.chatdemo.chat.dto;

import com.acc.chatdemo.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomDto {
    private Long id;
    private String name;
    private ChatMessageDto lastMessage;
}
