package com.baekji.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequestDTO {
    @JsonProperty("user_nickname")
    private String userNickname;     // 변경할 닉네임
    @JsonProperty("user_email")
    private String userEmail;        // 변경할 이메일
}