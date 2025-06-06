package com.baekji.study.repository;

import com.baekji.study.domain.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {

    // userId로 시험 일정 조회
    List<ExamSchedule> findByUserUserId(Long userId);

    // userId + 시험명 검색
    List<ExamSchedule> findByUserUserIdAndExamScheduleNameContaining(Long userId, String examScheduleName);
}