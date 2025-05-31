package com.baekji.subject.service;

import com.baekji.subject.domain.Subject;
import com.baekji.subject.dto.SubjectDTO;
import com.baekji.subject.mapper.SubjectMapper;
import com.baekji.subject.repository.SubjectRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        if (subjects.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_SUBJECT);
        }
        return subjectMapper.toSubjectDTOList(subjects);
    }

    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));
        return subjectMapper.toSubjectDTO(subject);
    }
}
