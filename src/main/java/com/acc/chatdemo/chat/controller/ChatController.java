package com.acc.chatdemo.chat.controller;

import com.acc.chatdemo.chat.dto.ChatMessageDto;
import com.acc.chatdemo.chat.dto.ChatRoomDto;
import com.acc.chatdemo.chat.dto.CreateChatRoomDto;
import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.mapper.ChatMessageDtoMapper;
import com.acc.chatdemo.chat.mapper.ChatRoomDtoMapper;
import com.acc.chatdemo.chat.service.ChatMessageService;
import com.acc.chatdemo.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatRoomDtoMapper chatRoomDtoMapper;
    private final ChatMessageDtoMapper chatMessageDtoMapper;

    @GetMapping
    public ResponseEntity<Page<ChatRoomDto>> getChatRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ChatRoom> chatRooms = chatRoomService.getChatRooms(page, size);
        return ResponseEntity.ok(chatRoomDtoMapper.toDto(chatRooms));
    }

    @GetMapping("{chatRoomId}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        return ResponseEntity.ok(chatRoomDtoMapper.toDto(chatRoom));
    }

    @GetMapping("{chatRoomId}/messages")
    public ResponseEntity<Page<ChatMessageDto>> getMessages(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ChatMessage> messages = chatMessageService.getChatMessagesByChatRoomId(chatRoomId, page, size);
        return ResponseEntity.ok(chatMessageDtoMapper.toDto(messages));
    }

    @PostMapping
    public ResponseEntity<ChatRoomDto> createChatRoom(
            @RequestBody CreateChatRoomDto dto
    ) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(dto.getName());
        return ResponseEntity.ok(chatRoomDtoMapper.toDto(chatRoom));
    }

    @GetMapping("{chatRoomId}/last-message")
    public ResponseEntity<ChatMessageDto> getLastMessage(@PathVariable Long chatRoomId) {
        ChatMessage lastMessage = chatRoomService.findLastMessage(chatRoomId);
        return ResponseEntity.ok(chatMessageDtoMapper.toDto(lastMessage));
    }
}
