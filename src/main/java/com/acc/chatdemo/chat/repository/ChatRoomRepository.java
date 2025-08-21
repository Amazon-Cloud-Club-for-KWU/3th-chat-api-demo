package com.acc.chatdemo.chat.repository;

import com.acc.chatdemo.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
