package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.CreateExamScheduleRequestDTO;
import com.baekji.study.dto.ExamScheduleDTO;
import com.baekji.study.dto.UpdateExamScheduleRequestDTO;
import com.baekji.study.service.ExamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-schedules")
@RequiredArgsConstructor
public class ExamScheduleController {

    private final ExamScheduleService examScheduleService;

    // 설명.1.1 시험 일정 전체 조회
    @GetMapping
    public ResponseDTO<List<ExamScheduleDTO>> getAllExamSchedules() {
        List<ExamScheduleDTO> response = examScheduleService.getAllExamSchedules();
        return ResponseDTO.ok(response);
    }

    // 설명.1.2 시험 id로 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<ExamScheduleDTO> getExamScheduleById(@PathVariable("id") Long id) {
        ExamScheduleDTO response = examScheduleService.getExamScheduleById(id);
        return ResponseDTO.ok(response);
    }

    // 설명.1.3 시험명으로 조회
    @GetMapping("/search/{exam_schedule_name}")
    public ResponseDTO<List<ExamScheduleDTO>> getExamSchedulesByName(@PathVariable("exam_schedule_name") String examScheduleName) {
        List<ExamScheduleDTO> response = examScheduleService.getExamSchedulesByName(examScheduleName);
        return ResponseDTO.ok(response);
    }

    // 설명.2. 시험 일정 등록
    @PostMapping
    public ResponseDTO<ExamScheduleDTO> createExamSchedule(@RequestBody CreateExamScheduleRequestDTO createDTO) {
        ExamScheduleDTO created = examScheduleService.createExamSchedule(createDTO);
        return ResponseDTO.ok(created);
    }

    // 설명.3. 시험 일정 수정
    @PutMapping("/{id}")
    public ResponseDTO<ExamScheduleDTO> updateExamSchedule(@PathVariable("id") Long id,
                                                           @RequestBody UpdateExamScheduleRequestDTO updateDTO) {
        ExamScheduleDTO updated = examScheduleService.updateExamSchedule(id, updateDTO);
        return ResponseDTO.ok(updated);
    }

    // 설명.4. 시험 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseDTO<String> deleteExamSchedule(@PathVariable("id") Long id) {
        examScheduleService.deleteExamSchedule(id);

        return ResponseDTO.ok("일정이 삭제되었습니다. ");
    }
}
