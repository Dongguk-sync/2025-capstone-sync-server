package com.baekji.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotHistoryDTO {

    @JsonProperty("chat_bot_history_id")
    private Long chatBotHistoryId;

    @JsonProperty("history_first_question")
    private String historyFirstQuestion;

    @JsonProperty("history_created_at")
    private LocalDateTime historyCreatedAt;

    @JsonProperty("user_id")
    private Long userId;
}
