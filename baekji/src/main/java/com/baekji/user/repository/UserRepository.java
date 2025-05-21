package com.baekji.user.repository;

import com.baekji.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 기본 CRUD 메서드 포함됨
}
