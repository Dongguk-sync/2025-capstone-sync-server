package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.service.StudysService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studys")
@RequiredArgsConstructor
public class StudysController {

    private final StudysService studysService;

    //설명.1.1. 학습 일정 ID로 전체 학습 조회
    @GetMapping("/study-schedule-id/{study_schedule_id}")
    public ResponseDTO<List<StudysDTO>> getAllStudysByScheduleId(@PathVariable("study_schedule_id") Long scheduleId) {
        List<StudysDTO> response = studysService.getAllStudysByScheduleId(scheduleId);
        return ResponseDTO.ok(response);
    }

    //설명.1.2. 학습 ID로 단일 학습 조회
    @GetMapping("/{id}")
    public ResponseDTO<StudysDTO> getStudysById(@PathVariable Long id) {
        StudysDTO response = studysService.getStudysById(id);
        return ResponseDTO.ok(response);
    }
}
