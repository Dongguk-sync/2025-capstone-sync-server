package com.baekji.study.repository;

import com.baekji.study.domain.Studys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudysRepository extends JpaRepository<Studys, Long> {
}
