package com.baekji.security;

import com.baekji.user.repository.UserRepository;
import com.baekji.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;
    private final UserRepository userRepository;
    private final Environment env;
    private final JwtUtil jwtUtil;


    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder,  UserService userService
            , Environment env, JwtUtil jwtUtil
    ,UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.userRepository=userRepository;
        this.env = env;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // 설명.CORS 처리 및 CSRF 비활성화
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable);

        // 설명. AuthenticationManager setup
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        // 설명. loadUserByUsername 호출
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        // 설명. 인증 매니저 생성
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // 설명. 권한 설정
        http.authorizeHttpRequests(authz -> authz
                    .requestMatchers(new AntPathRequestMatcher("/api/health", "GET")).permitAll()
                    // 설명. 1. 로그인은 어떤 사용자도 이용 가능
                    .requestMatchers(new AntPathRequestMatcher("/api/login/**", "POST")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/auth/**", "POST")).permitAll()

                    // 설명. 2. user(회원) 도메인
                    // 설명. 2.1. 회원 테이블 관련 API
                    .requestMatchers(new AntPathRequestMatcher("/api/users/**", "GET")).hasAnyRole("USER", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/api/users/**", "POST")).hasAnyRole("USER", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/api/users/**", "DELETE")).hasAnyRole("USER", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/api/users/**", "PUT")).hasAnyRole("USER", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/api/users/**", "PATCH")).hasAnyRole("USER", "ADMIN")


                    // 그외.. 테이블 관련 api
                    // 설명. 기타 요청은 인증 필요
                    .anyRequest().authenticated()
                )
                // 설명. authenticationManager 등록
                .authenticationManager(authenticationManager)
                // 설명. 세션 관리 설정 (JWT 사용 시)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 설명. 인증 필터 등록
                .addFilter(getAuthenticationFilter(authenticationManager))
                // 설명. JWT 필터 추가
                .addFilterBefore(new JwtFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 커스텀 인증 필터 설정 (로그인 URL 변경)
    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter
                = new AuthenticationFilter(authenticationManager,userRepository, env, bCryptPasswordEncoder);
        authenticationFilter.setFilterProcessesUrl("/api/login"); // 로그인 처리 URL 변경
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return authenticationFilter;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /* 설명. CORS 설정 추가 */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);

        configuration.setAllowedOrigins(List.of("https://inflow.run")); // Allow frontend
//        configuration.setAllowedOrigins(List.of("http://localhost:5173/")); // 로컬
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}