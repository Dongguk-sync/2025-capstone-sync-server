package com.baekji.study.service;

import com.baekji.common.ResponseDTO;
import com.baekji.study.domain.StudyMessage;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.repository.StudyMessageRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyMessageService {

    private final StudyMessageRepository studyMessageRepository;
    private final ModelMapper modelMapper;


    //설명.1.1 id로 조회하기
    public StudyMessageDTO getStudyMessageById(Long id) {
        StudyMessage message = studyMessageRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_MESSAGE));
        return modelMapper.map(message, StudyMessageDTO.class);
    }

    // 설명.1.2 학습별로 메시지 리스트 조회
    public List<StudyMessageDTO> getMessagesByStudysId(Long studysId) {
        List<StudyMessage> messages = studyMessageRepository.findAllByStudys_StudysIdOrderBySmCreatedAtAsc(studysId);
        return messages.stream()
                .map(msg -> modelMapper.map(msg, StudyMessageDTO.class))
                .collect(Collectors.toList());
    }


}
