package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.dto.StudyMessageRegisterRequestDTO;
import com.baekji.study.service.StudyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-messages")
@RequiredArgsConstructor
public class StudyMessageController {

    private final StudyMessageService studyMessageService;

    //설명.1.1 id로 조회하기
    @GetMapping("/id/{id}")
    public ResponseDTO<StudyMessageDTO> getStudyMessageById(@PathVariable Long id) {
        StudyMessageDTO response = studyMessageService.getStudyMessageById(id);
        return ResponseDTO.ok(response);
    }

    // 설명.1.2 학습별로 메시지 리스트 조회
    @GetMapping("/studys-id/{studysId}")
    public ResponseDTO<List<StudyMessageDTO>> getMessagesByStudysId(@PathVariable Long studysId) {
        List<StudyMessageDTO> response = studyMessageService.getMessagesByStudysId(studysId);
        return ResponseDTO.ok(response);
    }

    // 설명.2 교안별 메시지 등록
    @PostMapping("/lecture/chat")
    public ResponseDTO<StudyMessageDTO> registerStudyMessage(@RequestBody StudyMessageRegisterRequestDTO request) {
        StudyMessageDTO response = studyMessageService.registerMessageFlow(request);
        return ResponseDTO.ok(response);
    }

}
