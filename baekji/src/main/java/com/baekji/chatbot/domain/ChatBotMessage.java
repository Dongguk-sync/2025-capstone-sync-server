package com.baekji.chatbot.domain;

import com.baekji.common.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_bot_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBotMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long botMessageId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType; // AI, HUMAN

    @Column(nullable = false, columnDefinition = "TEXT")
    private String messageContent;

    @Column(nullable = false)
    private LocalDateTime messageCreatedAt;

    private String subjectName;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_bot_history_id")
    private ChatBotHistory chatBotHistory;
}
