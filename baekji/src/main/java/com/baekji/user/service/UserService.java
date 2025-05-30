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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        return userMapper.toUserDTOList(users);
    }

}
