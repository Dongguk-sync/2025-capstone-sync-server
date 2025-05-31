package com.baekji.security.generator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 비밀번호 제조기
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "baekji";  // 평문 비밀번호
        String encodedPassword = encoder.encode(rawPassword);  // 암호화된 비밀번호
        System.out.println("Encoded password: " + encodedPassword);
    }
}

