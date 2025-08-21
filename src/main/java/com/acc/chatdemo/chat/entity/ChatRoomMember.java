package com.acc.chatdemo.chat.entity;

import com.acc.chatdemo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_chat_room_member",
                columnNames = {"chat_room_id", "user_id"}
        )
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "last_read_message_id")
    private ChatMessage lastReadMessage;
}
