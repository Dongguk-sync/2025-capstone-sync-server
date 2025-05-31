package com.baekji.chatbot.dto;

import com.baekji.common.enums.MessageType;
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
public class ChatBotMessageDTO {

    @JsonProperty("bot_message_id")
    private Long botMessageId;

    @JsonProperty("message_type")
    private MessageType messageType;

    @JsonProperty("message_content")
    private String messageContent;

    @JsonProperty("message_created_at")
    private LocalDateTime messageCreatedAt;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("chat_bot_history_id")
    private Long chatBotHistoryId;
}
