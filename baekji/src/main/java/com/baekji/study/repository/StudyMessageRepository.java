package com.baekji.study.repository;

import com.baekji.study.domain.StudyMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMessageRepository extends JpaRepository<StudyMessage, Long> {

    // studyMessageRepository
    void deleteAllByStudys_StudysId(Long studysId);

    List<StudyMessage> findAllByStudys_StudysIdOrderBySmCreatedAtAsc(Long studysId);

}
