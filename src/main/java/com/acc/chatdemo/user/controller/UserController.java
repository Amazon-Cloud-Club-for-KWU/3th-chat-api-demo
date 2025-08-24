package com.acc.chatdemo.user.controller;

import com.acc.chatdemo.chat.dto.ChatRoomDto;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.service.ChatRoomService;
import com.acc.chatdemo.user.dto.UserDto;
import com.acc.chatdemo.user.entity.User;
import com.acc.chatdemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.acc.chatdemo.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @GetMapping("chat-rooms")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms() {
        Long userId = getCurrentUserId();
        List<ChatRoom> chatRooms = chatRoomService.getJoinedChatRooms(userId);
        return ResponseEntity.ok(chatRooms.stream()
                .map(ChatRoomDto::toDto)
                .toList());
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(UserDto.toDto(user));
    }
}
