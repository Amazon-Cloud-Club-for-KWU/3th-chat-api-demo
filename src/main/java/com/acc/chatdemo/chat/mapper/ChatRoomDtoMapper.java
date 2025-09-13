package com.acc.chatdemo.chat.mapper;

import com.acc.chatdemo.chat.dto.ChatRoomDto;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.utils.AbstractDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomDtoMapper extends AbstractDtoMapper<ChatRoom, ChatRoomDto> {
    @Override
    public ChatRoomDto toDto( ChatRoom entity) {
        return ChatRoomDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
