package com.acc.chatdemo.user.dto;

import com.acc.chatdemo.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
}
