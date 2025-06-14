package com.baekji.user.domain;

import com.baekji.common.enums.UserRole;
import com.baekji.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor  // <== 기본 생성자
@AllArgsConstructor // <== 전체 필드 생성자 (Builder 필수)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_email", nullable = false, length = 255)
    private String userEmail;

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 255)
    private UserRole userRole= UserRole.USER;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false, length = 255)
    private UserStatus userStatus=UserStatus.ACTIVE;

    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;

    @Builder.Default
    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime userCreatedAt = LocalDateTime.now().withNano(0);

    @Column(name = "user_profile_url", nullable = false, columnDefinition = "TEXT")
    private String userProfileUrl;

    @Column(name = "user_phone_number", nullable = false, length = 255)
    private String userPhoneNumber;

    @Column(name = "user_nickname", length = 10)
    private String userNickname;

    @Builder.Default
    @Column(name = "user_last_logged_in", nullable = false)
    private LocalDateTime userLastLoggedIn = LocalDateTime.of(2000, 1, 1, 10, 0, 0);

    @Builder.Default
    @Column(name = "user_studied_days", nullable = false)
    private Long userStudiedDays = 0L;

    @Builder.Default
    @Column(name = "user_total_studys", nullable = false)
    private Long userTotalStudys = 0L;

    @Builder.Default
    @Column(name = "user_completed_studys", nullable = false)
    private Long userCompletedStudys = 0L;

    @Builder.Default
    @Column(name = "user_progress_rate", nullable = false)
    private Double userProgressRate = 0.0;
}