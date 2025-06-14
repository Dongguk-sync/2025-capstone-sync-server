package com.baekji.study.service;

import com.baekji.chatbot.dto.AILectureMessageDTO;
import com.baekji.common.ResponseDTO;
import com.baekji.common.enums.MessageType;
import com.baekji.study.domain.StudyMessage;
import com.baekji.study.domain.Studys;
import com.baekji.study.dto.AIResponseDTO;
import com.baekji.study.dto.AIServerRequestDTO;
import com.baekji.study.dto.StudyMessageDTO;
import com.baekji.study.dto.StudyMessageRegisterRequestDTO;
import com.baekji.study.repository.StudyMessageRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.study.repository.StudysRepository;
import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.domain.Subject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyMessageService {

    private final StudyMessageRepository studyMessageRepository;
    private final StudysRepository studysRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;


    @Value("${ai.server-url}")
    private String aiServerUrl; // ex: http://localhost:8000

    //설명.1.1 id로 조회하기
    public StudyMessageDTO getStudyMessageById(Long id) {
        StudyMessage message = studyMessageRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_MESSAGE));
        return modelMapper.map(message, StudyMessageDTO.class);
    }

    // 설명.1.2 학습별로 메시지 리스트 조회
    public List<StudyMessageDTO> getMessagesByStudysId(Long studysId) {
        List<StudyMessage> messages = studyMessageRepository.findAllByStudys_StudysIdOrderBySmCreatedAtAsc(studysId);
        return messages.stream()
                .map(msg -> modelMapper.map(msg, StudyMessageDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.2 교안별 메시지 등록
    @Transactional
    public StudyMessageDTO registerMessageFlow(StudyMessageRegisterRequestDTO request) {
        // 1. 학습 정보 조회
        Studys studys = studysRepository.findById(request.getStudysId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDYS));

        AnswerFile file = studys.getStudySchedule().getAnswerFile();
        Subject subject = studys.getStudySchedule().getSubject();


        List<StudyMessage> messages =studyMessageRepository.findAllByStudys_StudysIdOrderBySmCreatedAtAsc(studys.getStudysId());

        // 2. 사용자 메시지 저장 (HUMAN)
        StudyMessage humanMessage = StudyMessage.builder()
                .studys(studys)
                .messageType(MessageType.HUMAN)
                .smContent(request.getQuestion())
                .smSubjectName(subject.getSubjectName())
                .smFileName(file.getFileName())
                .smFileUrl(file.getFileUrl())
                .smCreatedAt(LocalDateTime.now().withNano(0))
                .build();
        studyMessageRepository.save(humanMessage);

        // 3. AI 요청 DTO 구성
        AIServerRequestDTO aiRequest = new AIServerRequestDTO();
        aiRequest.setQuestion(request.getQuestion());
        aiRequest.setSubjectName(subject.getSubjectName());
        aiRequest.setFileName(file.getFileName());
        aiRequest.setFileContent(file.getFileContent());
        aiRequest.setStudysSttContent(studys.getStudysSttContent());
        aiRequest.setStudysFeedContent(studys.getStudysFeedContent());
        aiRequest.setChatBotHistoryId("studys_"+request.getStudysId());

        List<AILectureMessageDTO> chatHistory = messages.stream()
                .map(m -> {
                    AILectureMessageDTO dto = new AILectureMessageDTO();
                    dto.setMessageType(m.getMessageType().name());
                    dto.setMessageContent(m.getSmContent());
                    return dto;
                })
                .collect(Collectors.toList());

        aiRequest.setChatBotHistory(chatHistory);


        // 4. AI 서버 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AIServerRequestDTO> entity = new HttpEntity<>(aiRequest, headers);

        String aiUrl = aiServerUrl + "/chat/lecture_chat";
        ResponseEntity<String> aiResponse = restTemplate.postForEntity(aiUrl, entity, String.class);

        // 응답 로그 찍기
        System.out.println("AI Raw Response Body: " + aiResponse.getBody());

        ObjectMapper mapper = new ObjectMapper();
        AIResponseDTO ai;
        try {
            JsonNode root = mapper.readTree(aiResponse.getBody());
            JsonNode contentNode = root.get("content");

            ai = mapper.treeToValue(contentNode, AIResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace(); // 구체적인 예외 메시지 확인
            throw new CommonException(ErrorCode.AI_SERVER_ERROR);
        }


        // 6. AI 메시지 저장
        StudyMessage aiMessage = StudyMessage.builder()
                .studys(studys)
                .messageType(MessageType.AI)
                .smContent(ai.getMessageContent())
                .smSubjectName(subject.getSubjectName())
                .smFileName(file.getFileName())
                .smFileUrl(file.getFileUrl())
                .smCreatedAt(LocalDateTime.now().withNano(0))
                .build();
        aiMessage = studyMessageRepository.save(aiMessage);

        // 7. 응답 반환
        StudyMessageDTO response = StudyMessageDTO.builder()
                .smId(aiMessage.getSmId())
                .studysId(studys.getStudysId())
                .messageType(aiMessage.getMessageType())
                .smContent(aiMessage.getSmContent())
                .smSubjectName(subject.getSubjectName())
                .smFileName(file.getFileName())
                .smFileUrl(file.getFileUrl())
                .smCreatedAt(aiMessage.getSmCreatedAt())
                .build();

        return response;
    }


}
