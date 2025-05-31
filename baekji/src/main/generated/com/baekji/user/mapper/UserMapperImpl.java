package com.baekji.user.mapper;

import com.baekji.user.domain.UserEntity;
import com.baekji.user.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-31T16:35:59+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.userId( user.getUserId() );
        userDTO.userEmail( user.getUserEmail() );
        userDTO.userPassword( user.getUserPassword() );
        userDTO.userName( user.getUserName() );
        userDTO.userCreatedAt( user.getUserCreatedAt() );
        userDTO.userProfileUrl( user.getUserProfileUrl() );
        userDTO.userPhoneNumber( user.getUserPhoneNumber() );
        userDTO.userNickname( user.getUserNickname() );
        userDTO.userLastLoggedIn( user.getUserLastLoggedIn() );
        userDTO.userStudiedDays( user.getUserStudiedDays() );
        userDTO.userTotalReviews( user.getUserTotalReviews() );
        userDTO.userCompletedReviews( user.getUserCompletedReviews() );
        userDTO.userProgressRate( user.getUserProgressRate() );

        return userDTO.build();
    }

    @Override
    public List<UserDTO> toUserDTOList(List<UserEntity> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( UserEntity userEntity : users ) {
            list.add( toUserDTO( userEntity ) );
        }

        return list;
    }
}
