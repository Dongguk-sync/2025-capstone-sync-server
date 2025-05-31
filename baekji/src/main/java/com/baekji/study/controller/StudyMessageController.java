package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.service.StudyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-messages")
@RequiredArgsConstructor
public class StudyMessageController {

    private final StudyMessageService studyMessageService;

    @GetMapping
    public ResponseDTO<List<StudyMessageDTO>> getAllStudyMessages() {
        List<StudyMessageDTO> response = studyMessageService.getAllStudyMessages();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<StudyMessageDTO> getStudyMessageById(@PathVariable Long id) {
        StudyMessageDTO response = studyMessageService.getStudyMessageById(id);
        return ResponseDTO.ok(response);
    }
}
