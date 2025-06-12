package com.baekji.subject.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.dto.AnswerResponseFileDTO;
import com.baekji.subject.service.AnswerFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/answer-files")
@RequiredArgsConstructor
public class AnswerFileController {

    private final AnswerFileService answerFileService;

    // 설명.1.1. 과목별 교안 전체 조회
    @GetMapping("/subject-id/{subjectId}")
    public ResponseDTO<List<AnswerResponseFileDTO>> getAnswerFilesBySubjectId(
            @PathVariable Long subjectId) {
        List<AnswerResponseFileDTO> response = answerFileService.getAnswerFilesBySubjectId(subjectId);
        return ResponseDTO.ok(response);
    }

    // 설명.1.2. 교안 id로 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<AnswerResponseFileDTO> getAnswerFileById(@PathVariable Long id) {
        AnswerResponseFileDTO response = answerFileService.getAnswerFileById(id);
        return ResponseDTO.ok(response);
    }

    // 설명.1.3. 사용자별 교안 전체 조회
    @GetMapping("/user-id/{userId}")
    public ResponseDTO<List<AnswerResponseFileDTO>> getAnswerFilesByUserId(
            @PathVariable("userId") Long userId) {
        List<AnswerResponseFileDTO> response = answerFileService.getAnswerFilesByUserId(userId);
        return ResponseDTO.ok(response);
    }


    // 설명.1.4. 사용자별 교안명으로 조회
    @GetMapping("/search/user-id/{user_id}/name/{file_name}")
    public ResponseDTO<List<AnswerResponseFileDTO>> getAnswerFilesByUserIdAndFileName(
            @PathVariable("user_id") Long userId,
            @PathVariable("file_name") String fileName) {
        List<AnswerResponseFileDTO> response = answerFileService.getAnswerFilesByUserIdAndFileName(userId, fileName);
        return ResponseDTO.ok(response);
    }

    //설명.2. 교안 등록
    @PostMapping("/upload")
    public ResponseDTO<AnswerFileDTO> uploadAnswerFile(
            @RequestParam("user_id") Long userId,
            @RequestParam("subject_id") Long subjectId,
            @RequestParam("file") MultipartFile file) {

        AnswerFileDTO response = answerFileService.saveAnswerFile(userId,subjectId, file);
        return ResponseDTO.ok(response);
    }

    //설명.3. 교안 삭제
    @DeleteMapping("/id/{file_id}")
    public ResponseDTO<String> deleteAnswerFile(@PathVariable("file_id") Long fileId) {
        answerFileService.deleteAnswerFile(fileId);
        return ResponseDTO.ok("교안이 삭제되었습니다.");
    }


}
