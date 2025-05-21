package com.baekji.user.domain;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "User")
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
}
