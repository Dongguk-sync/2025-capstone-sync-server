package com.baekji.subject.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.subject.dto.SubjectCreateRequest;
import com.baekji.subject.dto.SubjectDTO;
import com.baekji.subject.dto.SubjectUpdateRequest;
import com.baekji.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    // 설명.1.1. 사용자별 과목 전체 조회
    @GetMapping("/user-id/{user_id}")
    public ResponseDTO<List<SubjectDTO>> getAllSubjectsByUserId(@PathVariable("user_id") Long userId) {
        List<SubjectDTO> response = subjectService.getAllSubjectsByUserId(userId);
        return ResponseDTO.ok(response);
    }

    // 설명.1.2. 과목 id로 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO response = subjectService.getSubjectById(id);
        return ResponseDTO.ok(response);
    }

    // 설명.1.3. 사용자별 과목명으로 조회
    @GetMapping("/search/user-id/{user_id}/name/{subject_name}")
    public ResponseDTO<List<SubjectDTO>> getSubjectsByUserIdAndSubjectName(@PathVariable("user_id") Long userId,
                                                                           @PathVariable("subject_name") String subjectName) {
        List<SubjectDTO> response = subjectService.getSubjectsByUserIdAndSubjectName(userId, subjectName);
        return ResponseDTO.ok(response);
    }


    // 설명.2. 과목 생성
    @PostMapping
    public ResponseDTO<SubjectDTO> createSubject(@RequestBody SubjectCreateRequest request) {
        SubjectDTO response = subjectService.createSubject(request);
        return ResponseDTO.ok(response);
    }


    // 설명.3. 과목 수정 (이름 변경)
    @PutMapping("/id/{id}")
    public ResponseDTO<SubjectDTO> updateSubject(@PathVariable("id")Long id, @RequestBody SubjectUpdateRequest request) {
        SubjectDTO response = subjectService.updateSubject(id, request);
        return ResponseDTO.ok(response);
    }


    // 설명.4. 과목 삭제
    @DeleteMapping("/id/{id}")
    public ResponseDTO<String> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseDTO.ok("과목이 삭제 되었습니다. 관련 교안도 모두 삭제되었습니다.");
    }
}
