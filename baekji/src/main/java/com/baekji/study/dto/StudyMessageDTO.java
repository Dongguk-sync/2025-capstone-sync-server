package com.baekji.study.dto;

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
public class StudyMessageDTO {

    @JsonProperty("sm_id")
    private Long smId;

    @JsonProperty("message_type")
    private MessageType messageType;

    @JsonProperty("sm_content")
    private String smContent;

    @JsonProperty("sm_subject_name")
    private String smSubjectName;

    @JsonProperty("sm_file_name")
    private String smFileName;

    @JsonProperty("sm_file_url")
    private String smFileUrl;

    @JsonProperty("sm_created_at")
    private LocalDateTime smCreatedAt;

    @JsonProperty("studys_id")
    private Long studysId;
}
