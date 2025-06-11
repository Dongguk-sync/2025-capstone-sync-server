package com.baekji.study.repository;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.study.domain.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {

      List<StudySchedule> findAllByUser_UserIdAndStudyScheduleDate(Long userId, LocalDate date);

}
