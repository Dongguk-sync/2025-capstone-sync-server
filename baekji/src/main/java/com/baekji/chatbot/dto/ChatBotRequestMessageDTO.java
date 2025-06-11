package com.baekji.chatbot.dto;

import com.baekji.common.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//설명. 챗봇 대화 DTO
public class ChatBotRequestMessageDTO {
    @JsonProperty("message_type")
    private MessageType messageType;

    @JsonProperty("message_content")
    private String messageContent;

    @JsonProperty("chat_bot_history_id")
    private Long chatBotHistoryId;

    @JsonProperty("user_id")
    private Long userId;
}
