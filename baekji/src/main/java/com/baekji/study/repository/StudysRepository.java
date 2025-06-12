package com.baekji.study.repository;

import com.baekji.study.domain.Studys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudysRepository extends JpaRepository<Studys, Long> {
    List<Studys> findAllByStudySchedule_StudyScheduleId(Long scheduleId);

    List<Studys> findAllByAnswerFile_FileId(Long fileId);

    @Query("SELECT COALESCE(MAX(s.studysRound), 0) FROM Studys s WHERE s.studySchedule.studyScheduleId = :scheduleId")
    Long findMaxRoundByScheduleId(@Param("scheduleId") Long scheduleId);

}
