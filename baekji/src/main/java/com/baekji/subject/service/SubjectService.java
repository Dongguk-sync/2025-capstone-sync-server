package com.baekji.subject.service;

import com.baekji.subject.domain.Subject;
import com.baekji.subject.dto.SubjectCreateRequest;
import com.baekji.subject.dto.SubjectDTO;
import com.baekji.subject.dto.SubjectUpdateRequest;
import com.baekji.subject.repository.SubjectRepository;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.repository.UserRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 설명.1.1. 사용자별 과목 전체 조회
    public List<SubjectDTO> getAllSubjectsByUserId(Long userId) {
        List<Subject> subjects = subjectRepository.findByUserUserId(userId);
        if (subjects.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_SUBJECT);
        }
        return subjects.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.1.2. 과목 id로 조회
    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));
        return modelMapper.map(subject, SubjectDTO.class);
    }

    // 설명.1.3. 사용자별 과목명으로 조회
    public List<SubjectDTO> getSubjectsByUserIdAndSubjectName(Long userId, String subjectName) {
        List<Subject> subjects = subjectRepository.findByUserUserIdAndSubjectNameContaining(userId, subjectName);
        if (subjects.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_SUBJECT);
        }
        return subjects.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.2. 과목 생성
    @Transactional
    public SubjectDTO createSubject(SubjectCreateRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Subject subject = Subject.builder()
                .subjectName(request.getSubjectName())
                .createdAt(LocalDateTime.now().withNano(0))
                .user(user)
                .build();

        Subject savedSubject = subjectRepository.save(subject);
        return modelMapper.map(savedSubject, SubjectDTO.class);
    }

    // 설명.3. 과목 수정 (이름, 시각 변경)
    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectUpdateRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));

        subject.setSubjectName(request.getSubjectName());
        subject.setCreatedAt(LocalDateTime.now().withNano(0));

        Subject updatedSubject = subjectRepository.save(subject);
        return modelMapper.map(updatedSubject, SubjectDTO.class);
    }

    // 설명.4. 과목 삭제
    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));

        subjectRepository.delete(subject);
        // 관련 교안 (AnswerFile) 삭제는 Cascade 설정 되어 있으면 자동, 아니면 추가 필요
    }
}
