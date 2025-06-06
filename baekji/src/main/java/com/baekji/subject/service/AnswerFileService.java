package com.baekji.subject.service;

import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.repository.AnswerFileRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerFileService {

    private final AnswerFileRepository answerFileRepository;
    private final ModelMapper modelMapper;

    // 설명.1.1. 과목별 교안 전체 조회
    public List<AnswerFileDTO> getAnswerFilesBySubjectId(Long subjectId) {
        List<AnswerFile> answerFiles = answerFileRepository.findBySubjectSubjectId(subjectId);
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFiles.stream()
                .map(file -> modelMapper.map(file, AnswerFileDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.1.2. 교안 id로 조회
    public AnswerFileDTO getAnswerFileById(Long id) {
        AnswerFile answerFile = answerFileRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE));
        return modelMapper.map(answerFile, AnswerFileDTO.class);
    }

    // 설명.1.3. 사용자별 교안 전체 조회
    public List<AnswerFileDTO> getAnswerFilesByUserId(Long userId) {
        List<AnswerFile> answerFiles = answerFileRepository.findBySubjectUserUserId(userId);
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFiles.stream()
                .map(file -> modelMapper.map(file, AnswerFileDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.1.4. 사용자별 교안명으로 조회
    public List<AnswerFileDTO> getAnswerFilesByUserIdAndFileName(Long userId, String fileName) {
        List<AnswerFile> answerFiles = answerFileRepository.findBySubjectUserUserIdAndFileNameContaining(userId, fileName);
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFiles.stream()
                .map(file -> modelMapper.map(file, AnswerFileDTO.class))
                .collect(Collectors.toList());
    }
}
