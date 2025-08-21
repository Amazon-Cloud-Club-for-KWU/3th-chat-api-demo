package com.acc.chatdemo.websocket;

import com.acc.chatdemo.chat.dto.CreateChatMessageDto;
import com.acc.chatdemo.chat.service.ChatMessageService;
import com.acc.chatdemo.chat.service.ChatRoomService;
import com.acc.chatdemo.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RedisPublisher redisPublisher;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload CreateChatMessageDto dto, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        chatMessageService.sendMessage(dto, userId);
    }

    @MessageMapping("/chat/join/{roomId}")
    public void joinRoom(@DestinationVariable String roomId, Principal principal) {
        chatRoomService.joinChatRoom(Long.parseLong(roomId), Long.parseLong(principal.getName()));
    }

    @MessageMapping("/chat/leave/{roomId}")
    public void leaveRoom(@DestinationVariable String roomId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        
        // DB에서 멤버십 제거
        chatRoomService.leaveChatRoom(Long.parseLong(roomId), userId);
        
        // 클라이언트에게 구독 해제 명령 전송
        messagingTemplate.convertAndSendToUser(
            principal.getName(),
            "/queue/unsubscribe", 
            roomId
        );
    }
}