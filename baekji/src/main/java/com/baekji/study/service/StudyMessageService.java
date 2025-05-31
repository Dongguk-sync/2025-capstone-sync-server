package com.baekji.study.service;

import com.baekji.study.domain.StudyMessage;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.mapper.StudyMessageMapper;
import com.baekji.study.repository.StudyMessageRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMessageService {

    private final StudyMessageRepository studyMessageRepository;
    private final StudyMessageMapper studyMessageMapper;

    public List<StudyMessageDTO> getAllStudyMessages() {
        List<StudyMessage> messages = studyMessageRepository.findAll();
        if (messages.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_STUDY_MESSAGE);
        }
        return studyMessageMapper.toStudyMessageDTOList(messages);
    }

    public StudyMessageDTO getStudyMessageById(Long id) {
        StudyMessage message = studyMessageRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_MESSAGE));
        return studyMessageMapper.toStudyMessageDTO(message);
    }
}
