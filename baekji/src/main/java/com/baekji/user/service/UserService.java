package com.baekji.user.service;

import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.mapper.UserMapper;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // 설명.1.1. 전체 사용자 조회
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        return userMapper.toUserDTOList(users);
    }

    // 설명.1.2. ID로 사용자 조회
    public UserDTO getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return userMapper.toUserDTO(user);
    }

    // 설명.1.3. 이메일로 사용자 조회
    public UserDTO getUserByEmail(String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return userMapper.toUserDTO(user);
    }
}
