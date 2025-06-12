package com.baekji.study.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 선언 시, 매칭 안 되는 필드 무
public class AIResponseDTO {

    @JsonProperty("message_type")
    private String messageType;

    @JsonProperty("message_content")
    private String messageContent;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("file_name")
    private String fileName;
}
