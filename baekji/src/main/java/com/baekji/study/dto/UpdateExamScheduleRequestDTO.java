package com.baekji.study.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateExamScheduleRequestDTO {

    @JsonProperty("exam_schedule_date")
    private LocalDateTime examScheduleDate;

    @JsonProperty("exam_schedule_name")
    private String examScheduleName;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("user_id")
    private Long userId;
}
