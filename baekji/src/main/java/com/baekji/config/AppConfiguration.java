package com.baekji.config;


import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.domain.Subject;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.dto.SubjectDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.modelmapper.PropertyMap;

import java.util.List;

@Configuration
public class AppConfiguration {

    // 설명. ModelMapper 설정
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Subject -> SubjectDTO 매핑 규칙
        modelMapper.addMappings(new PropertyMap<Subject, SubjectDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
            }
        });

        // AnswerFile -> AnswerFileDTO 매핑 규칙
        modelMapper.addMappings(new PropertyMap<AnswerFile, AnswerFileDTO>() {
            @Override
            protected void configure() {
                map().setSubjectId(source.getSubject().getSubjectId());
            }
        });

        return modelMapper;
    }


    /* 설명. Security 자체에서 사용할 암호화 방식용 bean 추가 */
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 설명. RestTemplate 추가 */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /* 설명. CORS 설정 추가 */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//
//        configuration.setAllowedOrigins(List.of("http://localhost:5173/")); // Allow frontend
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
