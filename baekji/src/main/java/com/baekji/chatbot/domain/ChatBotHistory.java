package com.baekji.chatbot.domain;

import com.baekji.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_bot_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBotHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatBotHistoryId;

    private String historyFirstQuestion;

    private LocalDateTime historyCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
