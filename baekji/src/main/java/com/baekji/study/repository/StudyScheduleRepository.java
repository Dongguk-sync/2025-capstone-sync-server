package com.baekji.study.repository;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.study.domain.StudySchedule;
import com.baekji.subject.domain.AnswerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {

      List<StudySchedule> findAllByUser_UserId(Long userId);

      List<StudySchedule> findAllByUser_UserIdAndStudyScheduleDate(Long userId, LocalDate date);
      @Query("SELECT ss FROM StudySchedule ss " +
              "WHERE ss.user.userId = :userId " +
              "AND ss.subject.subjectName LIKE %:subjectName%")
      List<StudySchedule> findByUserIdAndSubjectNameContaining(@Param("userId") Long userId,
                                                                   @Param("subjectName") String subjectName);

      Optional<StudySchedule> findTopByAnswerFileOrderByStudyScheduleDateDesc(AnswerFile answerFile);

      @Query("SELECT ss FROM StudySchedule ss " +
              "WHERE ss.user.userId = :userId " +
              "AND ss.studyScheduleDate BETWEEN :startDate AND :endDate")
      List<StudySchedule> findByUserIdAndDateRange(
              @Param("userId") Long userId,
              @Param("startDate") LocalDate startDate,
              @Param("endDate") LocalDate endDate);
}
