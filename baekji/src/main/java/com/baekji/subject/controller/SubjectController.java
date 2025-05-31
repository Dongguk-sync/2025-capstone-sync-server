package com.baekji.subject.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.subject.dto.SubjectDTO;
import com.baekji.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseDTO<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> response = subjectService.getAllSubjects();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO response = subjectService.getSubjectById(id);
        return ResponseDTO.ok(response);
    }
}
