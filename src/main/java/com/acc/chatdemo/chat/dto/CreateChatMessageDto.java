package com.acc.chatdemo.chat.dto;

import io.github.springwolf.core.asyncapi.annotations.AsyncApiPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatMessageDto {
    private String content;
    private Long chatRoomId;
}
