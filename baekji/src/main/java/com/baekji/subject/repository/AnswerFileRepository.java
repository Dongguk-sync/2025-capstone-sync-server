package com.baekji.subject.repository;

import com.baekji.subject.domain.AnswerFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerFileRepository extends JpaRepository<AnswerFile, Long> {
    // 과목별 교안 전체 조회
    List<AnswerFile> findBySubjectSubjectId(Long subjectId);

    // 사용자별 교안 전체 조회
    List<AnswerFile> findBySubjectUserUserId(Long userId);

    // 사용자별 교안명 검색
    List<AnswerFile> findBySubjectUserUserIdAndFileNameContaining(Long userId, String fileName);}
