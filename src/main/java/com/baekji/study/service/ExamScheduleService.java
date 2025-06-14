package com.baekji.study.service;

import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.study.domain.ExamSchedule;
import com.baekji.study.dto.CreateExamScheduleRequestDTO;
import com.baekji.study.dto.ExamScheduleDTO;
import com.baekji.study.dto.UpdateExamScheduleRequestDTO;
import com.baekji.study.repository.ExamScheduleRepository;
import com.baekji.subject.domain.Subject;
import com.baekji.subject.repository.SubjectRepository;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    // 시험 일정 전체 조회
    public List<ExamScheduleDTO> getAllExamSchedules(Long userId) {
        List<ExamSchedule> exams = examScheduleRepository.findByUserUserId(userId);
        if (exams.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE);
        }
        return exams.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 시험 일정 ID로 조회
    public ExamScheduleDTO getExamScheduleById(Long id) {
        ExamSchedule exam = examScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE));
        return convertToDTO(exam);
    }

    // 시험명으로 조회
    public List<ExamScheduleDTO> getExamSchedulesByName(Long userId, String examScheduleName){
        List<ExamSchedule> exams = examScheduleRepository.findByUserUserIdAndExamScheduleNameContaining(userId, examScheduleName);
        if (exams.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE);
        }
        return exams.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 시험 일정 등록
    @Transactional
    public ExamScheduleDTO createExamSchedule(CreateExamScheduleRequestDTO examScheduleDTO) {
        Subject subject = subjectRepository.findById(examScheduleDTO.getSubjectId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));
        UserEntity user = userRepository.findById(examScheduleDTO.getUserId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        ExamSchedule examSchedule = ExamSchedule.builder()
                .examScheduleDate(examScheduleDTO.getExamScheduleDate())
                .examScheduleName(examScheduleDTO.getExamScheduleName())
                .subject(subject)
                .user(user)
                .build();

        ExamSchedule savedExam = examScheduleRepository.save(examSchedule);
        return convertToDTO(savedExam);
    }

    // 시험 일정 수정
    @Transactional
    public ExamScheduleDTO updateExamSchedule(Long id, UpdateExamScheduleRequestDTO examScheduleDTO) {
        ExamSchedule existingExam = examScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE));

        Subject subject = subjectRepository.findById(examScheduleDTO.getSubjectId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));
        UserEntity user = userRepository.findById(examScheduleDTO.getUserId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        existingExam.setExamScheduleDate(examScheduleDTO.getExamScheduleDate());
        existingExam.setExamScheduleName(examScheduleDTO.getExamScheduleName());
        existingExam.setSubject(subject);
        existingExam.setUser(user);

        ExamSchedule updatedExam = examScheduleRepository.save(existingExam);
        return convertToDTO(updatedExam);
    }

    // 시험 일정 삭제
    @Transactional
    public void deleteExamSchedule(Long id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_EXAM_SCHEDULE));
        examScheduleRepository.delete(examSchedule);
    }

    // Entity -> DTO 변환 (수동 매핑)
    private ExamScheduleDTO convertToDTO(ExamSchedule examSchedule) {
        return ExamScheduleDTO.builder()
                .examScheduleId(examSchedule.getExamScheduleId())
                .examScheduleDate(examSchedule.getExamScheduleDate())
                .examScheduleName(examSchedule.getExamScheduleName())
                .subjectId(examSchedule.getSubject() != null ? examSchedule.getSubject().getSubjectId() : null)
                .userId(examSchedule.getUser() != null ? examSchedule.getUser().getUserId() : null)
                .build();
    }
}
