package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudyScheduleDTO;
import com.baekji.study.service.StudyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-schedules")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleService studyScheduleService;

    @GetMapping
    public ResponseDTO<List<StudyScheduleDTO>> getAllStudySchedules() {
        List<StudyScheduleDTO> response = studyScheduleService.getAllStudySchedules();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<StudyScheduleDTO> getStudyScheduleById(@PathVariable Long id) {
        StudyScheduleDTO response = studyScheduleService.getStudyScheduleById(id);
        return ResponseDTO.ok(response);
    }
}
