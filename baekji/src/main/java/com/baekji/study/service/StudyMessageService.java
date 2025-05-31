package com.baekji.study.service;

import com.baekji.study.domain.StudyMessage;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.repository.StudyMessageRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyMessageService {

    private final StudyMessageRepository studyMessageRepository;
    private final ModelMapper modelMapper;

    public List<StudyMessageDTO> getAllStudyMessages() {
        List<StudyMessage> messages = studyMessageRepository.findAll();
        if (messages.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_STUDY_MESSAGE);
        }
        return messages.stream()
                .map(message -> modelMapper.map(message, StudyMessageDTO.class))
                .collect(Collectors.toList());
    }

    public StudyMessageDTO getStudyMessageById(Long id) {
        StudyMessage message = studyMessageRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_MESSAGE));
        return modelMapper.map(message, StudyMessageDTO.class);
    }
}
