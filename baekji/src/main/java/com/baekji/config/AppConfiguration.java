package com.baekji.config;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.domain.ChatBotMessage;
import com.baekji.chatbot.dto.ChatBotHistoryDTO;
import com.baekji.chatbot.dto.ChatBotMessageDTO;
import com.baekji.study.domain.StudyMessage;
import com.baekji.study.domain.StudySchedule;
import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.dto.StudyScheduleDTO;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.dto.StudysResponseDTO;
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

        // Subject → SubjectDTO
        modelMapper.addMappings(new PropertyMap<Subject, SubjectDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
            }
        });

        // SubjectDTO → Subject
        modelMapper.addMappings(new PropertyMap<SubjectDTO, Subject>() {
            @Override
            protected void configure() {
                // 연관된 User는 service에서 직접 set
                skip(destination.getUser());
            }
        });

        // AnswerFile → AnswerFileDTO
        modelMapper.addMappings(new PropertyMap<AnswerFile, AnswerFileDTO>() {
            @Override
            protected void configure() {
                map().setSubjectId(source.getSubject().getSubjectId());
            }
        });

        // AnswerFileDTO → AnswerFile
        modelMapper.addMappings(new PropertyMap<AnswerFileDTO, AnswerFile>() {
            @Override
            protected void configure() {
                // 연관된 Subject는 service에서 직접 set
                skip(destination.getSubject());
            }
        });

        // ChatBotHistory → ChatBotHistoryDTO
        modelMapper.addMappings(new PropertyMap<ChatBotHistory, ChatBotHistoryDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
            }
        });

        // ChatBotHistoryDTO → ChatBotHistory
        modelMapper.addMappings(new PropertyMap<ChatBotHistoryDTO, ChatBotHistory>() {
            @Override
            protected void configure() {
                skip(destination.getUser());
            }
        });

        // ChatBotMessage → ChatBotMessageDTO
        modelMapper.addMappings(new PropertyMap<ChatBotMessage, ChatBotMessageDTO>() {
            @Override
            protected void configure() {
                map().setChatBotHistoryId(source.getChatBotHistory().getChatBotHistoryId());
            }
        });

        // ChatBotMessageDTO → ChatBotMessage
        modelMapper.addMappings(new PropertyMap<ChatBotMessageDTO, ChatBotMessage>() {
            @Override
            protected void configure() {
                skip(destination.getChatBotHistory());
            }
        });

        // StudySchedule → StudyScheduleDTO
        modelMapper.addMappings(new PropertyMap<StudySchedule, StudyScheduleDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
                map().setFileId(source.getAnswerFile().getFileId());
                map().setSubjectId(source.getSubject().getSubjectId());
            }
        });

        // StudyScheduleDTO → StudySchedule
        modelMapper.addMappings(new PropertyMap<StudyScheduleDTO, StudySchedule>() {
            @Override
            protected void configure() {
                skip(destination.getUser());
                skip(destination.getAnswerFile());
                skip(destination.getSubject());
            }
        });

        // Studys → StudysDTO
        modelMapper.addMappings(new PropertyMap<Studys, StudysDTO>() {
            @Override
            protected void configure() {
                map().setStudyScheduleId(source.getStudySchedule().getStudyScheduleId());
            }
        });

        // StudysDTO → Studys
        modelMapper.addMappings(new PropertyMap<StudysDTO, Studys>() {
            @Override
            protected void configure() {
                skip(destination.getStudySchedule());
            }
        });

        // StudyMessage → StudyMessageDTO
        modelMapper.addMappings(new PropertyMap<StudyMessage, StudyMessageDTO>() {
            @Override
            protected void configure() {
                map().setStudysId(source.getStudys().getStudySchedule().getStudyScheduleId());
            }
        });

        // Studys → StudysResponseDTO
        modelMapper.addMappings(new PropertyMap<Studys, StudysResponseDTO>() {
            @Override
            protected void configure() {
                map().setStudyScheduleId(source.getStudySchedule().getStudyScheduleId());

                // 아래는 studySchedule을 통해 subject, answerFile에서 값을 추출
                map().setSubjectId(source.getStudySchedule().getSubject().getSubjectId());
                map().setSubjectName(source.getStudySchedule().getSubject().getSubjectName());

                map().setFileId(source.getStudySchedule().getAnswerFile().getFileId());
                map().setFileName(source.getStudySchedule().getAnswerFile().getFileName());
            }
        });

        // StudyMessageDTO → StudyMessage
        modelMapper.addMappings(new PropertyMap<StudyMessageDTO, StudyMessage>() {
            @Override
            protected void configure() {
                skip(destination.getStudys()); // 연관 관계는 서비스에서 set
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
