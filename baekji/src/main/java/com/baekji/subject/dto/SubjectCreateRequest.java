package com.baekji.subject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCreateRequest {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("subject_name")
    private String subjectName;
}
