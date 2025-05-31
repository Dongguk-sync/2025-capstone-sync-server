package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.ExamScheduleDTO;
import com.baekji.study.service.ExamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedules")
@RequiredArgsConstructor
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    @GetMapping
    public ResponseDTO<List<ExamScheduleDTO>> getAllExamSchedules() {
        List<ExamScheduleDTO> response = examScheduleService.getAllExamSchedules();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<ExamScheduleDTO> getExamScheduleById(@PathVariable Long id) {
        ExamScheduleDTO response = examScheduleService.getExamScheduleById(id);
        return ResponseDTO.ok(response);
    }
}
