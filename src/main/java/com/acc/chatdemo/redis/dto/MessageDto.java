package com.acc.chatdemo.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private String roomId;
    private String sender;
    private String content;
    private String timestamp;
}
