package com.acc.chatdemo.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessTokenDto {
    private String accessToken;
}
