package com.baekji.study.service;

import com.baekji.study.domain.ExamSchedule;
import com.baekji.study.dto.ExamScheduleDTO;
import com.baekji.study.mapper.ExamScheduleMapper;
import com.baekji.study.repository.ExamScheduleRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;
    private final ExamScheduleMapper examScheduleMapper;

    public List<ExamScheduleDTO> getAllExamSchedules() {
        List<ExamSchedule> exams = examScheduleRepository.findAll();
        if (exams.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE);
        }
        return examScheduleMapper.toExamScheduleDTOList(exams);
    }

    public ExamScheduleDTO getExamScheduleById(Long id) {
        ExamSchedule exam = examScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE));
        return examScheduleMapper.toExamScheduleDTO(exam);
    }
}
