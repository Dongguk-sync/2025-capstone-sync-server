package com.baekji.study.repository;

import com.baekji.study.domain.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
}
