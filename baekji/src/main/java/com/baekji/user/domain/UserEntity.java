package com.baekji.user.domain;

import com.baekji.common.enums.UserRole;
import com.baekji.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_email", nullable = false, length = 255)
    private String userEmail;

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 255)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false, length = 255)
    private UserStatus userStatus;

    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;

    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime userCreatedAt;

    @Column(name = "user_profile_url", nullable = false, columnDefinition = "TEXT")
    private String userProfileUrl;

    @Column(name = "user_phone_number", nullable = false, length = 255)
    private String userPhoneNumber;

    @Column(name = "user_nickname", length = 10)
    private String userNickname;

    @Column(name = "user_last_logged_in", nullable = false)
    private LocalDateTime userLastLoggedIn = LocalDateTime.of(2000, 1, 1, 10, 0, 0);

    @Column(name = "user_studied_days", nullable = false)
    private Long userStudiedDays = 1L;

    @Column(name = "user_total_reviews", nullable = false)
    private Long userTotalReviews = 0L;

    @Column(name = "user_completed_reviews", nullable = false)
    private Long userCompletedReviews = 0L;

    @Column(name = "user_progress_rate", nullable = false)
    private Double userProgressRate = 0.0;
}