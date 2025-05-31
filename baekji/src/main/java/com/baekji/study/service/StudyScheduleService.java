package com.baekji.study.service;

import com.baekji.study.domain.StudySchedule;
import com.baekji.study.dto.StudyScheduleDTO;
import com.baekji.study.mapper.StudyScheduleMapper;
import com.baekji.study.repository.StudyScheduleRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyScheduleMapper studyScheduleMapper;

    public List<StudyScheduleDTO> getAllStudySchedules() {
        List<StudySchedule> schedules = studyScheduleRepository.findAll();
        if (schedules.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_STUDY_SCHEDULE);
        }
        return studyScheduleMapper.toStudyScheduleDTOList(schedules);
    }

    public StudyScheduleDTO getStudyScheduleById(Long id) {
        StudySchedule schedule = studyScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_SCHEDULE));
        return studyScheduleMapper.toStudyScheduleDTO(schedule);
    }
}
