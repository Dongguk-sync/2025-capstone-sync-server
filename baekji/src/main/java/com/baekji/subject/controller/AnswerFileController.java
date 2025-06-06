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

    // 설명.1.1. 과목별 교안 전체 조회
    // 설명.1.1. 과목별 교안 전체 조회
    @GetMapping("/subject-id/{subjectId}")
    public ResponseDTO<List<AnswerFileDTO>> getAnswerFilesBySubjectId(
            @PathVariable Long subjectId) {
        List<AnswerFileDTO> response = answerFileService.getAnswerFilesBySubjectId(subjectId);
        return ResponseDTO.ok(response);
    }

    // 설명.1.2. 교안 id로 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<AnswerFileDTO> getAnswerFileById(@PathVariable Long id) {
        AnswerFileDTO response = answerFileService.getAnswerFileById(id);
        return ResponseDTO.ok(response);
    }

    // 설명.1.3. 사용자별 교안 전체 조회
    @GetMapping("/user-id/{userId}")
    public ResponseDTO<List<AnswerFileDTO>> getAnswerFilesByUserId(
            @PathVariable("userId") Long userId) {
        List<AnswerFileDTO> response = answerFileService.getAnswerFilesByUserId(userId);
        return ResponseDTO.ok(response);
    }

    // 설명.1.4. 사용자별 교안명으로 조회
    @GetMapping("/search/user-id/{user_id}/name/{file_name}")
    public ResponseDTO<List<AnswerFileDTO>> getAnswerFilesByUserIdAndFileName(
            @PathVariable("user_id") Long userId,
            @PathVariable("file_name") String fileName) {
        List<AnswerFileDTO> response = answerFileService.getAnswerFilesByUserIdAndFileName(userId, fileName);
        return ResponseDTO.ok(response);
    }


}
