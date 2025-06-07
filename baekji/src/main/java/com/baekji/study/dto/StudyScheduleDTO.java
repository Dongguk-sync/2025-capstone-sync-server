package com.baekji.study.dto;

import com.baekji.common.enums.COMPLECTED;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyScheduleDTO {

    @JsonProperty("study_schedule_id")
    private Long studyScheduleId;

    @JsonProperty("study_schedule_date")
    private LocalDate studyScheduleDate;

    @JsonProperty("study_schedule_completed")
    private COMPLECTED studyScheduleCompleted;

    @JsonProperty("study_schedule_created_at")
    private LocalDateTime studyScheduleCreatedAt;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("file_id")
    private Long fileId;

    @JsonProperty("subject_id")
    private Long subjectId;
}
