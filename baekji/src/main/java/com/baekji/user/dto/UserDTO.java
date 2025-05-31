package com.baekji.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_password")
    private String userPassword;

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

    @JsonProperty("user_total_reviews")
    private Long userTotalReviews;

    @JsonProperty("user_completed_reviews")
    private Long userCompletedReviews;

    @JsonProperty("user_progress_rate")
    private Double userProgressRate;
}
