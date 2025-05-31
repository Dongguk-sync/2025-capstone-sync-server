package com.baekji.user.dto;

import com.baekji.common.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDTO {

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_password")
    private String userPassword;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_phone_number")
    private String userPhoneNumber;

    @JsonProperty("user_nickname")
    private String userNickname;

    @JsonProperty("user_role")
    private String userRole;
}
