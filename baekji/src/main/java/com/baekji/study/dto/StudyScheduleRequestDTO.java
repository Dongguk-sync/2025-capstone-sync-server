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
public class StudyScheduleRequestDTO {

    @JsonProperty("study_schedule_date")
    private LocalDate studyScheduleDate;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("file_id")
    private Long fileId;

    @JsonProperty("subject_id")
    private Long subjectId;
}

