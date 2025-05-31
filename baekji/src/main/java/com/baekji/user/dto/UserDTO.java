package com.baekji.user.dto;


import com.baekji.common.enums.UserRole;
import com.baekji.common.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_password")
    private String userPassword;

    @JsonProperty("user_role")
    private UserRole userRole;

    @JsonProperty("user_status")
    private UserStatus userStatus;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_created_at")
    private LocalDateTime userCreatedAt;

    @JsonProperty("user_profile_url")
    private String userProfileUrl;

    @JsonProperty("user_phone_number")
    private String userPhoneNumber;

    @JsonProperty("user_nickname")
    private String userNickname;

    @JsonProperty("user_last_logged_in")
    private LocalDateTime userLastLoggedIn;

    @JsonProperty("user_studied_days")
    private Long userStudiedDays;

    @JsonProperty("user_total_studys")
    private Long userTotalStudys;

    @JsonProperty("user_completed_studys")
    private Long userCompletedStudys;

    @JsonProperty("user_progress_rate")
    private Double userProgressRate;
}
