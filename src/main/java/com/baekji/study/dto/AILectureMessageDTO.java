package com.baekji.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AILectureMessageDTO {

    @JsonProperty("message_type")
    private String messageType; // "HUMAN" 또는 "AI"

    @JsonProperty("message_content")
    private String messageContent;
}
