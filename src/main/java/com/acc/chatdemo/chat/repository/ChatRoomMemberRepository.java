package com.acc.chatdemo.chat.repository;

import com.acc.chatdemo.chat.entity.ChatRoom;
import com.acc.chatdemo.chat.entity.ChatRoomMember;
import com.acc.chatdemo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
    Optional<ChatRoomMember> findByChatRoomAndUser(ChatRoom chatRoom, User user);
    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);
    List<ChatRoomMember> findAllByUser(User user);
}
