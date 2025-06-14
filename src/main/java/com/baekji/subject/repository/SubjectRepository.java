package com.baekji.subject.repository;

import com.baekji.subject.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // 사용자별 과목 전체 조회
    List<Subject> findByUserUserId(Long userId);

    // 사용자별 + 과목명 검색
    List<Subject> findByUserUserIdAndSubjectNameContaining(Long userId, String subjectName);
}
