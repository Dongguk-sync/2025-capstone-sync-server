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
    
    //설명.1.1 시험 일정 전체 조회
    @GetMapping
    public ResponseDTO<List<ExamScheduleDTO>> getAllExamSchedules() {
        List<ExamScheduleDTO> response = examScheduleService.getAllExamSchedules();
        return ResponseDTO.ok(response);
    }

    //설명.1.2. 시험id로 조회
    @GetMapping("/{id}")
    public ResponseDTO<ExamScheduleDTO> getExamScheduleById(@PathVariable Long id) {
        ExamScheduleDTO response = examScheduleService.getExamScheduleById(id);
        return ResponseDTO.ok(response);
    }

    //설명.1.3. 시험명으로 조회


    //설명.2. 시험 일정 등록

    //설명.3. 시험 일정 수정

    //설명.4. 시험 일정 삭제


}
