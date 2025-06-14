package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudyScheduleDTO;
import com.baekji.study.dto.StudyScheduleRequestDTO;
import com.baekji.study.dto.StudyScheduleResponseDTO;
import com.baekji.study.dto.StudyScheduleSearchDTO;
import com.baekji.study.service.StudyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-schedules")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleService studyScheduleService;

    //설명.1.1. 사용자별 학습 일정 전체 조회
    @GetMapping("/user-id/{user_id}")
    public ResponseDTO<List<StudyScheduleResponseDTO>> getAllStudySchedulesByUserId(@PathVariable("user_id") Long userId) {
        List<StudyScheduleResponseDTO> response = studyScheduleService.getAllStudySchedulesByUserId(userId);
        return ResponseDTO.ok(response);
    }


    //설명.1.2. 학습일정 id로 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<StudyScheduleResponseDTO> getStudyScheduleById(@PathVariable("id") Long id) {
        StudyScheduleResponseDTO response = studyScheduleService.getStudyScheduleById(id);
        return ResponseDTO.ok(response);
    }


    //설명.1.3. 사용자 ID + 과목명으로 학습일정 다건 조회
    @GetMapping("/search/user-id/{user_id}/name/{name}")
    public ResponseDTO<List<StudyScheduleResponseDTO>> getStudySchedulesByUserIdAndSubjectName(
            @PathVariable("user_id") Long userId,
            @PathVariable("name") String subjectName) {

        List<StudyScheduleResponseDTO> response = studyScheduleService.getByUserIdAndSubjectName(userId, subjectName);
        return ResponseDTO.ok(response);
    }

    //설명.1.4. 사용자 ID, 시작 날짜, 종료 날짜
    @PostMapping("/search/date")
    public ResponseDTO<List<StudyScheduleResponseDTO>> searchStudySchedules(@RequestBody StudyScheduleSearchDTO request) {
        List<StudyScheduleResponseDTO> result = studyScheduleService.searchSchedules(request);
        return ResponseDTO.ok(result);
    }

    //설명.2.1. 학습일정 등록
    @PostMapping
    public ResponseDTO<StudyScheduleDTO> createStudySchedule(@RequestBody StudyScheduleRequestDTO studyScheduleDTO) {
        StudyScheduleDTO response = studyScheduleService.createStudySchedule(studyScheduleDTO);
        return ResponseDTO.ok(response);
    }


    //설명.2.3. 학습일정 삭제
    @DeleteMapping("/id/{id}")
    public ResponseDTO<String> deleteStudySchedule(@PathVariable("id") Long studyScheduleId) {
        studyScheduleService.deleteStudyScheduleById(studyScheduleId);
        return ResponseDTO.ok("학습일정이 삭제되었습니다. 연관된 모든 학습들이 사라집니다.");
    }

}
