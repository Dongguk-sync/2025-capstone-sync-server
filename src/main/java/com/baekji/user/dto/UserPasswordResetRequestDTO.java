package com.baekji.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordResetRequestDTO {
    @JsonProperty("new_password")
    private String newPassword;
}