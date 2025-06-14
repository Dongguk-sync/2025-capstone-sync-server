package com.baekji.security;

import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.security.dto.AuthTokens;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.repository.UserRepository;
import com.baekji.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class JwtUtil {

    private final Key accessSecretKey;
    private final Key refreshSecretKey;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;
    private final UserService userService;
    private final UserRepository userRepository;

    public JwtUtil(
            @Value("${token.access-secret}") String accessSecretKey,
            @Value("${token.refresh-secret}") String refreshSecretKey,
            @Value("${token.access-expiration-time}") long accessExpirationTime,
            @Value("${token.refresh-expiration-time}") long refreshExpirationTime,
            UserService userService,
            UserRepository userRepository
    ) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
        this.accessSecretKey = Keys.hmacShaKeyFor(accessKeyBytes);

        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshKeyBytes);

        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.userService = userService;
        this.userRepository=userRepository;
    }

    // 설명. 리프레시 토큰으로 액세스 토큰 재발급하는 로직 처리
    // 설명. 리프레시 토큰으로 액세스 토큰 재발급
    public AuthTokens refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new CommonException(ErrorCode.EXPIRED_TOKEN_ERROR);
        }

        String userEmail =  getUserEmailFromRefresh(refreshToken);

        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String newAccessToken = generateAccessToken(user);

        return new AuthTokens(
                newAccessToken,
                refreshToken,
                "Bearer",
                getAccessTokenExpiration(),
                getRefreshTokenExpiration(),
                user.getUserId(),
                userEmail
        );
    }

    // 설명. 액세스 토큰 생성
    public String generateAccessToken(UserEntity user) {
        return Jwts.builder()
                .setSubject( user.getUserEmail())
                .claim("auth", List.of("ROLE_" + user.getUserRole().name()))
                .claim("UserId", user.getUserId())
                .claim("UserEmail", user.getUserEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(accessSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 설명. 리프레시 토큰 생성
    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .setSubject( user.getUserEmail())
                .claim("UserId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(refreshSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }



    // 설명. 액세스 토큰 검증
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.info("Invalid Access Token: {}", e.getMessage());
            throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
        }
    }

    // 설명. 리프레시 토큰 검증
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(refreshSecretKey).build().parseClaimsJws(token).getBody();
            return true;
        } catch (JwtException e) {
            log.info("Invalid Refresh Token: {}", e.getMessage());
            throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
        }
    }

    // 설명. Token에서 인증 객체 추출
    public Authentication getAuthentication(String token) {
        String userEmail = this.getUserEmailFromAccess(token);
        UserDetails userDetails  = userService.loadUserByUsername(userEmail);

        Claims claims = parseClaims(token);
        log.info("넘어온 AccessToken claims 확인: {}", claims);

        String authClaim = claims.get("auth") != null ? claims.get("auth").toString() : null;
        if (authClaim == null || authClaim.trim().isEmpty()) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities
                = Arrays.stream(authClaim.replace("[", "").replace("]", "").split(", "))
                .map(String::trim)
                .filter(role -> !role.isEmpty()) // 빈 문자열 제거
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 설명. accessToken에서 Claims 추출
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token).getBody();
    }

    // 설명. 토큰에서 employeeNumber 추출 (액세스 토큰)
    public String getUserEmailFromAccess(String token) {
        return Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 설명. 토큰에서 employeeNumber 추출 (리프레시 토큰)
    public String getUserEmailFromRefresh(String token) {
        return Jwts.parserBuilder().setSigningKey(refreshSecretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 설명. 액세스 토큰 만료 시간 가져오기
    public long getAccessTokenExpiration() {
        return System.currentTimeMillis() + accessExpirationTime;
    }

    // 설명. 리프레시 토큰 만료 시간 가져오기
    public long getRefreshTokenExpiration() {
        return System.currentTimeMillis() + refreshExpirationTime;
    }
}
