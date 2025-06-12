package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.dto.StudysResponseDTO;
import com.baekji.study.service.StudysService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studys")
@RequiredArgsConstructor
public class StudysController {

    private final StudysService studysService;
    

    //설명.1.1. 학습 ID로 단일 학습 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<StudysDTO> getStudysById(@PathVariable Long id) {
        StudysDTO response = studysService.getStudysById(id);
        return ResponseDTO.ok(response);
    }

    //설명.1.2. 사용자별 id로 전체 학습 조회
    @GetMapping("/study-schedule-id/{study_schedule_id}")
    public ResponseDTO<List<StudysResponseDTO>> getAllStudysByScheduleId(@PathVariable("study_schedule_id") Long scheduleId) {
        List<StudysResponseDTO> response = studysService.getAllStudysByScheduleId(scheduleId);
        return ResponseDTO.ok(response);
    }

    //설명.1.3. 교안별 전체 학습 조회
    @GetMapping("/file-id/{fileId}")
    public ResponseDTO<List<StudysResponseDTO>> getAllStudysByFileId(@PathVariable Long fileId) {
        List<StudysResponseDTO> response = studysService.getAllStudysByFileId(fileId);
        return ResponseDTO.ok(response);
    }

}
