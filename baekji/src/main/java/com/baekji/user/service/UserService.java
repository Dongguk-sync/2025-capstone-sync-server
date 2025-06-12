package com.baekji.user.service;

import com.baekji.common.enums.UserRole;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.user.dto.UserPasswordResetRequestDTO;
import com.baekji.user.dto.UserProfileUpdateRequestDTO;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.dto.UserDTO;
import com.baekji.user.dto.UserSignUpRequestDTO;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;

    @Value("${ai.server-url}")
    private String aiServerUrl; // ex: http://localhost:8000


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

        Optional<UserEntity> optionalUser = userRepository.findByUserEmail(signUpRequest.getUserEmail());

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            if (user.getUserEmail().equals(signUpRequest.getUserEmail())) {
                log.warn("이미 존재하는 이메일로 회원가입 시도: {}", signUpRequest.getUserEmail());
                throw new CommonException(ErrorCode.EXIST_USER);
            }

            if (user.getUserNickname().equals(signUpRequest.getUserNickname())) {
                log.warn("중복 닉네임으로 회원가입 시도: {}", signUpRequest.getUserNickname());
                throw new CommonException(ErrorCode.DUPLICATE_NICKNAME);
            }
        }

        String encodedPassword = encodePassword(signUpRequest.getUserPassword());
        log.debug("비밀번호 암호화 완료");

        UserEntity newUser = UserEntity.builder()
                .userEmail(signUpRequest.getUserEmail())
                .userPassword(encodedPassword)
                .userRole(UserRole.valueOf(signUpRequest.getUserRole().toUpperCase()))
                .userName(signUpRequest.getUserName())
                .userPhoneNumber(signUpRequest.getUserPhoneNumber())
                .userNickname(signUpRequest.getUserNickname())
                .userProfileUrl(basicUserProfileUrl)
                .build();

        UserEntity savedUser = userRepository.save(newUser);
        log.info("회원 저장 완료: {}", savedUser.getUserId());

        // AI 서버에 회원가입 정보 전달
        String url = aiServerUrl + "/signup";

        Map<String, String> request = new HashMap<>();
        String formattedUserId = "user_" + savedUser.getUserId(); // "user9"
        request.put("user_id", formattedUserId);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            boolean success = response != null && Boolean.TRUE.equals(response.get("success"));
            log.info("response: {}", response);
            if (success) {
                log.info("AI 서버로 회원가입 데이터 전달 성공: {}", savedUser.getUserId());
            } else {
                log.error("AI 서버 응답 실패 또는 success=false");
            }
        } catch (Exception e) {
            log.error("AI 서버 통신 중 예외 발생", e);
        }

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

    
    //설명.3. 프로필 설정
    // 설명.3. 닉네임, 이메일, 프로필 사진 수정

    @Transactional
    public UserDTO updateUserProfile(Long userId, UserProfileUpdateRequestDTO requestDTO, MultipartFile profileImage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 닉네임 중복 체크
        if (requestDTO.getUserNickname() != null && !requestDTO.getUserNickname().isEmpty()) {
            boolean isNicknameExist = userRepository.existsByUserNickname(requestDTO.getUserNickname());
            if (isNicknameExist && !requestDTO.getUserNickname().equals(user.getUserNickname())) {
                throw new CommonException(ErrorCode.DUPLICATE_NICKNAME);
            }
            user.setUserNickname(requestDTO.getUserNickname());
        }

        // 이메일 변경
        if (requestDTO.getUserEmail() != null && !requestDTO.getUserEmail().isEmpty()) {
            user.setUserEmail(requestDTO.getUserEmail());
        }

        // 프로필 이미지 업로드
//        if (profileImage != null && !profileImage.isEmpty()) {
//            // 업로드 로직 작성 (예시: S3에 업로드하거나, 서버 파일 저장)
//            String uploadedUrl = uploadProfileImage(profileImage); // 이 부분 구현 필요
//            user.setUserProfileUrl(uploadedUrl);
//        }

        UserEntity updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }


    // 설명.4. 비밀번호 재설정
    @Transactional
    public UserDTO resetPassword(Long userId, UserPasswordResetRequestDTO requestDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String encodedPassword = encodePassword(requestDTO.getNewPassword());
        user.setUserPassword(encodedPassword);

        UserEntity updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
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
