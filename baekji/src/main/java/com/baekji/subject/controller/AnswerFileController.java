package com.baekji.subject.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.service.AnswerFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer-files")
@RequiredArgsConstructor
public class AnswerFileController {

    private final AnswerFileService answerFileService;

    @GetMapping
    public ResponseDTO<List<AnswerFileDTO>> getAllAnswerFiles() {
        List<AnswerFileDTO> response = answerFileService.getAllAnswerFiles();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<AnswerFileDTO> getAnswerFileById(@PathVariable Long id) {
        AnswerFileDTO response = answerFileService.getAnswerFileById(id);
        return ResponseDTO.ok(response);
    }
}
