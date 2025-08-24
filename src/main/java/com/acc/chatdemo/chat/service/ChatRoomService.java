package com.acc.chatdemo.chat.service;

import com.acc.chatdemo.chat.entity.ChatMessage;
import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.entity.ChatRoomMember;
import com.acc.chatdemo.chat.repository.ChatRoomMemberRepository;
import com.acc.chatdemo.chat.repository.ChatRoomRepository;
import com.acc.chatdemo.user.entity.User;
import com.acc.chatdemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public boolean isUserInChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + chatRoomId));
        User user = userService.getUserById(userId);
        return chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user);
    }

    @Transactional(readOnly = true)
    public Page<ChatRoom> getChatRooms(int page, int size) {
        return chatRoomRepository.findAll(
                PageRequest.of(page, size)
        );
    }

    @Transactional
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ChatMessage findLastMessage(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId);
        return chatRoom.getLastMessage();
    }

    @Transactional
    public ChatRoomMember upsertChatRoomMember(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId);
        ChatMessage lastMessage = findLastMessage(chatRoomId);
        User user = userService.getUserById(userId);
        if (chatRoomMemberRepository.existsByChatRoomAndUser(chatRoom, user)) {
            ChatRoomMember existingMember = chatRoomMemberRepository.findByChatRoomAndUser(chatRoom, user)
                    .orElseThrow(() -> new RuntimeException("Chat room member not found for chat room id: " + chatRoomId + " and user id: " + userId));
            existingMember.setLastReadMessage(lastMessage);
            return chatRoomMemberRepository.save(existingMember);
        }
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .lastReadMessage(lastMessage)
                .build();

        return chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId);
        User user = userService.getUserById(userId);

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndUser(chatRoom, user)
                .orElseThrow(() -> new RuntimeException("User is not a member of the chat room with id: " + chatRoomId));

        ChatMessage lastMessage = chatRoomMember.getLastReadMessage();
        chatRoomMember.setLastReadMessage(lastMessage);
    }

    @Transactional
    public void joinChatRoom(Long chatRoomId, Long userId) {

        upsertChatRoomMember(chatRoomId, userId);

    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getJoinedChatRooms(Long userId) {
        User user = userService.getUserById(userId);
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByUser(user);
        return chatRoomMembers.stream()
                .map(ChatRoomMember::getChatRoom)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(
                        chatRoom -> Optional.ofNullable(chatRoom.getLastMessage())
                                .map(ChatMessage::getCreatedAt)
                                .orElse(LocalDateTime.MIN),
                        Comparator.reverseOrder()
                ))
                .toList();
    }
}
