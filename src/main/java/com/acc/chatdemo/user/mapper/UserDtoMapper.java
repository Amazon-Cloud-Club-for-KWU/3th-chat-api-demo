package com.acc.chatdemo.user.mapper;

import com.acc.chatdemo.user.dto.UserDto;
import com.acc.chatdemo.user.entity.User;
import com.acc.chatdemo.utils.AbstractDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoMapper extends AbstractDtoMapper<User, UserDto> {

    @Override
    public UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .build();
    }
}
