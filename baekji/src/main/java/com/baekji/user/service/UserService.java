package com.baekji.user.service;

import com.baekji.common.enums.UserRole;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.dto.UserSignUpRequestDTO;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String basicUserProfileUrl = "https://baekji-bucket.s3.ap-northeast-2.amazonaws.com/user_basic_profile.png";
    // 설명.1.1. 전체 사용자 조회
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.1.2. ID로 사용자 조회
    public UserDTO getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return modelMapper.map(user, UserDTO.class);
    }


    // 설명.1.3. 이메일로 사용자 조회
    public UserDTO getUserByEmail(String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return modelMapper.map(user, UserDTO.class);
    }


    // 설명.2. 회원 가입
    @Transactional
    public UserDTO registerUser(UserSignUpRequestDTO signUpRequest) {
        if (userRepository.findByUserEmail(signUpRequest.getUserEmail()).isPresent()) {
            throw new CommonException(ErrorCode.EXIST_USER);
        }

        String encodedPassword = encodePassword(signUpRequest.getUserPassword());

        UserEntity newUser = UserEntity.builder()
                .userEmail(signUpRequest.getUserEmail())
                .userPassword(encodedPassword)
                .userRole(UserRole.valueOf(signUpRequest.getUserRole().toUpperCase())) // String → Enum
                .userName(signUpRequest.getUserName())
                .userPhoneNumber(signUpRequest.getUserPhoneNumber())
                .userNickname(signUpRequest.getUserNickname())
                .userProfileUrl(basicUserProfileUrl)
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        return modelMapper.map(savedUser, UserDTO.class);
    }


    // 설명.2.1. 비밀번호 Bcrypt 암호화
    private String encodePassword(String password) {
        try {
            return bCryptPasswordEncoder.encode(password);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.PASSWORD_ENCODING_FAILED);
        }
    }


    // 설명.5. 시큐리티를 위한 설정 메서드
    //  로그인 시 security가 자동으로 호출하는 메소드 */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        // 1. userEmail를 기준으로 사용자 조회
        UserEntity loginUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CommonException(ErrorCode.UNAUTHORIZED_ACCESS));

        // 2. 비밀번호 처리 (소셜 로그인 시 비밀번호가 없을 경우 기본값 설정)
        String encryptedPwd = loginUser.getUserPassword();
        if (encryptedPwd == null) {
            encryptedPwd = "{noop}";  // 비밀번호가 없을 경우 기본값 설정
        }

        // 3. 권한 정보를 userRole 필드에서 가져와서 변환
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                // "ROLE_EMPLOYEE" 또는 "ROLE_HR" 또는 "ROLE_MANAGER " 또는 "ROLE_ADMIN"
                new SimpleGrantedAuthority("ROLE_" + loginUser.getUserRole().name())
        );

        // 4. UserDetails 객체 반환
        return new User(loginUser.getUserEmail(), encryptedPwd,
                true, true, true, true,
                grantedAuthorities);
    }

}
