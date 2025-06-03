package com.baekji.study.repository;

import com.baekji.study.domain.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
    List<ExamSchedule> findByExamScheduleNameContaining(String name);
}
