package com.baekji.study.dto;

import com.baekji.chatbot.dto.ChatBotMessageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class StudyMessageRegisterRequestDTO {

    @JsonProperty("studys_id")
    private Long studysId;

    @JsonProperty("question")
    private String question;

}
