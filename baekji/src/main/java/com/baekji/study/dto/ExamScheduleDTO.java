package com.baekji.study.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleDTO {

    @JsonProperty("exam_schedule_id")
    private Long examScheduleId;

    @JsonProperty("exam_schedule_date")
    private LocalDate examScheduleDate;

    @JsonProperty("exam_schedule_name")
    private String examScheduleName;

    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("user_id")
    private Long userId;
}
