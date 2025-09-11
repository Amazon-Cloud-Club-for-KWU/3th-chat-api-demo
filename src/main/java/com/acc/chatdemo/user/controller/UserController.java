package com.acc.chatdemo.user.controller;

import com.acc.chatdemo.chat.dto.ChatRoomDto;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.mapper.ChatRoomDtoMapper;
import com.acc.chatdemo.chat.service.ChatRoomService;
import com.acc.chatdemo.user.dto.UserDto;
import com.acc.chatdemo.user.entity.User;
import com.acc.chatdemo.user.mapper.UserDtoMapper;
import com.acc.chatdemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import static com.acc.chatdemo.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatRoomDtoMapper chatRoomDtoMapper;
    private final UserDtoMapper userDtoMapper;

    @GetMapping("chat-rooms")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms() {
        Long userId = getCurrentUserId();
        List<ChatRoom> chatRooms = chatRoomService.getJoinedChatRooms(userId);
        return ResponseEntity.ok(chatRoomDtoMapper.toDto(chatRooms));
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(userDtoMapper.toDto(user));
    }
}
