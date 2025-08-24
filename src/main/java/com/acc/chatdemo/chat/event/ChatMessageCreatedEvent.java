package com.acc.chatdemo.chat.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageCreatedEvent {
    private final String messageId;
    private final Long roomId;
    private final Long senderId;
    private final String timestamp;
}