package com.acc.chatdemo.chat.controller;

import com.acc.chatdemo.chat.dto.CreateChatMessageDto;
import com.acc.chatdemo.chat.dto.CreateChatRoomDto;
import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.service.ChatMessageService;
import com.acc.chatdemo.chat.service.ChatRoomService;
import com.acc.chatdemo.common.PaginationResponse;
import com.acc.chatdemo.common.PaginationResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<PaginationResponse<ChatRoom>> getChatRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // This method should return a paginated list of chat rooms.
        // The actual implementation would involve calling a service method to fetch the chat rooms.
        // For now, we return an empty page as a placeholder.
        Page<ChatRoom> chatRooms = chatRoomService.getChatRooms(page, size);
        PaginationResponse<ChatRoom> response = PaginationResponseBuilder.build(chatRooms);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{chatRoomId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable Long chatRoomId) {
        // This method should return the chat room details for the given chat room ID.
        // The actual implementation would involve calling a service method to fetch the chat room details.
        // For now, we return a placeholder string
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("{chatRoomId}/messages")
    public ResponseEntity<PaginationResponse<ChatMessage>> getMessages(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // This method should return a paginated list of messages for the given chat room ID.
        // The actual implementation would involve calling a service method to fetch the messages.
        // For now, we return an empty page as a placeholder.
        Page<ChatMessage> messages = chatMessageService.getChatMessagesByChatRoomId(chatRoomId, page, size);
        PaginationResponse<ChatMessage> response = PaginationResponseBuilder.build(messages);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(
            @RequestBody CreateChatRoomDto dto
    ) {
        // This method should create a new chat message in the specified chat room.
        // The actual implementation would involve calling a service method to create the message.
        // For now, we return a placeholder string as a response.
        ChatRoom chatRoom = chatRoomService.createChatRoom(dto.getName());
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("{chatRoomId}/last-message")
    public ResponseEntity<ChatMessage> getLastMessage(@PathVariable Long chatRoomId) {
        // This method should return the last message in the specified chat room.
        // The actual implementation would involve calling a service method to fetch the last message.
        // For now, we return a placeholder string as a response.
        ChatMessage lastMessage = chatRoomService.findLastMessage(chatRoomId);
        return ResponseEntity.ok(lastMessage);
    }
}
