package com.baekji.user.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.security.JwtUtil;
import com.baekji.subject.dto.UserPasswordResetRequestDTO;
import com.baekji.subject.dto.UserProfileUpdateRequestDTO;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.dto.UserSignUpRequestDTO;
import com.baekji.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Environment env;
//    private final JwtUtil jwtUtil;


    // 설명.1.1. 사용자 전체 조회
    @GetMapping("/users/")
    public ResponseDTO<List<UserDTO>> getAllUsers() {
        List<UserDTO> response = userService.getAllUsers();
        return ResponseDTO.ok(response);
    }

    // 설명.1.2. 회원 정보 id로 조회
    @GetMapping("/users/id/{userId}")
    public ResponseDTO<UserDTO> getUserById(@PathVariable(value = "userId") Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseDTO.ok(user);
    }

    // 설명.1.3. 회원 정보 이메일로 조회
    @GetMapping("/users/email/{userEmail}")
    public ResponseDTO<UserDTO> getUserByEmail(@PathVariable(value = "userEmail") String userEmail) {
        UserDTO user = userService.getUserByEmail(userEmail);
        return ResponseDTO.ok(user);
    }


    // 설명.2. 회원 가입
    @PostMapping("/signup")
    public ResponseDTO<UserDTO> signUp(@RequestBody UserSignUpRequestDTO signUpRequestDTO) {
        UserDTO newUser = userService.registerUser(signUpRequestDTO);
        return ResponseDTO.ok(newUser);
    }
    // 설명.3. 프로필 수정
    // 프로필 수정
    @PatchMapping(value = "/users/{userId}/profile", consumes = {"multipart/form-data"})
    public ResponseDTO<UserDTO> updateProfile(@PathVariable Long userId,
                                              @RequestPart("userInfo") UserProfileUpdateRequestDTO requestDTO,
                                              @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        UserDTO updatedUser = userService.updateUserProfile(userId, requestDTO, profileImage);
        return ResponseDTO.ok(updatedUser);
    }
    // 설명.4. 비밀번호 재설정

    @PatchMapping("/users/{userId}/password")
    public ResponseDTO<UserDTO> resetPassword(@PathVariable Long userId,
                                              @RequestBody UserPasswordResetRequestDTO requestDTO) {
        UserDTO updatedUser = userService.resetPassword(userId, requestDTO);
        return ResponseDTO.ok(updatedUser);
    }
}
