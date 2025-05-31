package com.baekji.user.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 설명.1.1. 사용자 전체 조회
    @GetMapping
    public ResponseDTO<List<UserDTO>> getAllUsers() {
        List<UserDTO> response = userService.getAllUsers();
        return ResponseDTO.ok(response);
    }

    // 설명.1.2. 회원 정보 id로 조회
    @GetMapping("/id/{userId}")
    public ResponseDTO<UserDTO> getUserById(@PathVariable(value = "userId") Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseDTO.ok(user);
    }

    // 설명.1.3. 회원 정보 이메일로 조회
    @GetMapping("/email/{userEmail}")
    public ResponseDTO<UserDTO> getUserByEmail(@PathVariable(value = "userEmail") String userEmail) {
        UserDTO user = userService.getUserByEmail(userEmail);
        return ResponseDTO.ok(user);
    }

}
