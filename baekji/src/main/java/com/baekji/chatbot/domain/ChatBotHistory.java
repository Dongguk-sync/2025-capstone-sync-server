package com.baekji.chatbot.domain;

import com.baekji.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "chatBotHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatBotMessage> messages = new ArrayList<>();
}
