package com.baekji.user.service;

import com.baekji.user.domain.UserEntity;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream().map(user -> UserDTO.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userPassword(user.getUserPassword()) // 필요시 제거 또는 마스킹
                .userName(user.getUserName())
                .userCreatedAt(user.getUserCreatedAt())
                .userProfileUrl(user.getUserProfileUrl())
                .userPhoneNumber(user.getUserPhoneNumber())
                .userNickname(user.getUserNickname())
                .build()
        ).collect(Collectors.toList());
    }
}
