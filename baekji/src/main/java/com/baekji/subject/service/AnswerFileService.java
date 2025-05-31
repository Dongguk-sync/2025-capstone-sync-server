package com.baekji.subject.service;

import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.mapper.AnswerFileMapper;
import com.baekji.subject.repository.AnswerFileRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerFileService {

    private final AnswerFileRepository answerFileRepository;
    private final AnswerFileMapper answerFileMapper;

    public List<AnswerFileDTO> getAllAnswerFiles() {
        List<AnswerFile> answerFiles = answerFileRepository.findAll();
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFileMapper.toAnswerFileDTOList(answerFiles);
    }

    public AnswerFileDTO getAnswerFileById(Long id) {
        AnswerFile answerFile = answerFileRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE));
        return answerFileMapper.toAnswerFileDTO(answerFile);
    }
}
