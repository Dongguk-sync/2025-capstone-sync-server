package com.baekji.user.mapper;

import com.baekji.user.domain.UserEntity;
import com.baekji.user.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(UserEntity user);
    List<UserDTO> toUserDTOList(List<UserEntity> users);
}