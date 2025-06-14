package com.baekji.chatbot.dto;

import com.baekji.common.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//설명. 첫 질문 DTO
public class ChatBotFirstRequestMessageDTO {

    @JsonProperty("message_type")
    private MessageType messageType;

    @JsonProperty("message_content")
    private String messageContent;

    @JsonProperty("user_id")
    private Long userId;
}
