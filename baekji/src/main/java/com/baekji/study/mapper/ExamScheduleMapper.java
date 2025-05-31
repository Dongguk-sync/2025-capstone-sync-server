package com.baekji.study.mapper;

import com.baekji.study.domain.ExamSchedule;
import com.baekji.study.dto.ExamScheduleDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamScheduleMapper {
    ExamScheduleDTO toExamScheduleDTO(ExamSchedule examSchedule);
    List<ExamScheduleDTO> toExamScheduleDTOList(List<ExamSchedule> examSchedules);
}
