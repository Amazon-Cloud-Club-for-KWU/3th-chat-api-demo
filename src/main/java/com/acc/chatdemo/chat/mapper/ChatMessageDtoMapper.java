package com.acc.chatdemo.chat.mapper;

import com.acc.chatdemo.chat.dto.ChatMessageDto;
import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.user.mapper.UserDtoMapper;
import com.acc.chatdemo.utils.AbstractDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageDtoMapper extends AbstractDtoMapper<ChatMessage, ChatMessageDto> {
    private final UserDtoMapper userDtoMapper;

    @Override
    public ChatMessageDto toDto(ChatMessage entity) {
        return ChatMessageDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .seq(entity.getSeq())
                .sender(userDtoMapper.toDto(entity.getSender()))
                .build();
    }
}
