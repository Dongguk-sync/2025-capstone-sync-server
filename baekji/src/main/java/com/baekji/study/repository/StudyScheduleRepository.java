package com.baekji.study.repository;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.study.domain.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {

    long countByUser_UserIdAndStudyScheduleDate(Long userId, LocalDate date);

    long countByUser_UserIdAndStudyScheduleDateAndStudyScheduleCompleted(Long userId, LocalDate date, COMPLECTED completed);

}
