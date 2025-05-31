package com.baekji.study.mapper;

import com.baekji.study.domain.StudySchedule;
import com.baekji.study.dto.StudyScheduleDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudyScheduleMapper {
    StudyScheduleDTO toStudyScheduleDTO(StudySchedule studySchedule);
    List<StudyScheduleDTO> toStudyScheduleDTOList(List<StudySchedule> studySchedules);
}
