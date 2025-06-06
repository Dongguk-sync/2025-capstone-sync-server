package com.baekji.subject.service;

import com.baekji.subject.domain.Subject;
import com.baekji.subject.dto.SubjectDTO;
import com.baekji.subject.repository.SubjectRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    public List<SubjectDTO> getAllSubjectsByUserId(Long userId) {
        List<Subject> subjects = subjectRepository.findByUserUserId(userId);
        if (subjects.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_SUBJECT);
        }
        return subjects.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    public List<SubjectDTO> getSubjectsByUserIdAndSubjectName(Long userId, String subjectName) {
        List<Subject> subjects = subjectRepository.findByUserUserIdAndSubjectNameContaining(userId, subjectName);
        if (subjects.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_SUBJECT);
        }
        return subjects.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));
        return modelMapper.map(subject, SubjectDTO.class);
    }
}
