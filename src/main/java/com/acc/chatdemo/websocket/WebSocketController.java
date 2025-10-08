package com.acc.chatdemo.websocket;

import com.acc.chatdemo.chat.dto.CreateChatMessageDto;
import com.acc.chatdemo.chat.service.ChatMessageService;
import com.acc.chatdemo.chat.service.ChatRoomService;
import com.acc.chatdemo.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.acc.chatdemo.auth.JwtProvider;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RedisPublisher redisPublisher;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtProvider jwtProvider;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload CreateChatMessageDto dto, Principal principal, 
                           @Header("Authorization") String authHeader) {
        
        Long userId = getUserId(principal, authHeader);

        chatMessageService.sendMessage(dto, userId);
    }

    @MessageMapping("/chat/join/{roomId}")
    public void joinRoom(@DestinationVariable String roomId, Principal principal,
                        @Header("Authorization") String authHeader) {
        Long userId = getUserId(principal, authHeader);
        chatRoomService.joinChatRoom(Long.parseLong(roomId), userId);
    }

    @MessageMapping("/chat/leave/{roomId}")
    public void leaveRoom(@DestinationVariable String roomId, Principal principal,
                         @Header("Authorization") String authHeader) {
        Long userId = getUserId(principal, authHeader);
        
        chatRoomService.leaveChatRoom(Long.parseLong(roomId), userId);
        
        messagingTemplate.convertAndSendToUser(
            userId.toString(),
            "/queue/unsubscribe", 
            roomId
        );
    }
    
    private Long getUserId(Principal principal, String authHeader) {
        if (principal != null && principal.getName() != null) {
            return Long.parseLong(principal.getName());
        }
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtProvider.parseUserIdFromAccessToken(token);
        }
        
        throw new IllegalArgumentException("인증 정보를 찾을 수 없습니다.");
    }
}