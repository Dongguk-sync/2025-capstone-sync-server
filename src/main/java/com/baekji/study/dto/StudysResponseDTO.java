package com.baekji.study.dto;

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
public class StudysResponseDTO {
    @JsonProperty("studys_id")
    private Long studysId;

    @JsonProperty("studys_stt_content")
    private String studysSttContent;

    @JsonProperty("studys_feed_content")
    private String studysFeedContent;

    @JsonProperty("studys_subject_name")
    private String studysSubjectName;

    @JsonProperty("studys_created_at")
    private LocalDateTime studysCreatedAt;

    @JsonProperty("studys_round")
    private Integer studysRound;

    @JsonProperty("study_schedule_id")
    private Long studyScheduleId;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("file_id")
    private Long fileId;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("file_name")
    private String fileName;

}
