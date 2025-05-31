package com.baekji.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RequestLoginVO {

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("user_password")
    private String userPassword;
}
