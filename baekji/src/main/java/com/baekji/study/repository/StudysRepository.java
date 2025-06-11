package com.baekji.study.repository;

import com.baekji.study.domain.Studys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudysRepository extends JpaRepository<Studys, Long> {
    List<Studys> findAllByStudySchedule_StudyScheduleId(Long scheduleId);


}
