package com.baekji.study.service;

import com.baekji.study.domain.ExamSchedule;
import com.baekji.study.dto.ExamScheduleDTO;
import com.baekji.study.repository.ExamScheduleRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;
    private final ModelMapper modelMapper;

    public List<ExamScheduleDTO> getAllExamSchedules() {
        List<ExamSchedule> exams = examScheduleRepository.findAll();
        if (exams.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE);
        }
        return exams.stream()
                .map(exam -> modelMapper.map(exam, ExamScheduleDTO.class))
                .collect(Collectors.toList());
    }

    public ExamScheduleDTO getExamScheduleById(Long id) {
        ExamSchedule exam = examScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE));
        return modelMapper.map(exam, ExamScheduleDTO.class);
    }
}
