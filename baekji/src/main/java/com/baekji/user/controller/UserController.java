package com.baekji.user.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userController")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseDTO<List<UserDTO>> getAllUsers() {
        // 1.1. 설명: 사원 리스트 전체 조회
        List<UserDTO> response = userService.getAllUsers();

        return ResponseDTO.ok(response);
    }

}