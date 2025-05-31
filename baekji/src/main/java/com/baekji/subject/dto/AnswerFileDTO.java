package com.baekji.subject.dto;

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
public class AnswerFileDTO {

    @JsonProperty("file_id")
    private Long fileId;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_content")
    private String fileContent;

    @JsonProperty("file_type")
    private String fileType;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("subject_id")
    private Long subjectId;
}
