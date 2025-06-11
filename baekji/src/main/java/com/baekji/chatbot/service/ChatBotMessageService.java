package com.baekji.chatbot.service;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.domain.ChatBotMessage;
import com.baekji.chatbot.dto.ChatBotMessageDTO;
import com.baekji.chatbot.dto.ChatBotRequestMessageDTO;
import com.baekji.chatbot.repository.ChatBotHistoryRepository;
import com.baekji.chatbot.repository.ChatBotMessageRepository;
import com.baekji.common.enums.MessageType;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBotMessageService {

    private final ChatBotMessageRepository chatBotMessageRepository;
    private final ChatBotHistoryRepository chatBotHistoryRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Value("${ai.server-url}")
    private String aiServerUrl; // ex: http://localhost:8000

    public List<ChatBotMessageDTO> getMessagesByHistoryId(Long historyId) {
        List<ChatBotMessage> messages = chatBotMessageRepository.findByChatBotHistoryChatBotHistoryId(historyId);
        if (messages.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_CHATBOT_MESSAGE);
        }
        return messages.stream()
                .map(message -> modelMapper.map(message, ChatBotMessageDTO.class))
                .collect(Collectors.toList());
    }

    // 메시지 등록 (히스토리 새로 만들지 여부에 따라 분기)
    public ChatBotMessageDTO createMessage(ChatBotRequestMessageDTO dto) {
        ChatBotHistory history;

        if (dto.getChatBotHistoryId() == null) {
            // 1. 사용자 존재 확인
            UserEntity user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

            // 2. 첫 질문 요약 요청
            String summarized = summarizeFirstQuestion(dto.getMessageContent());

            // 3. 히스토리 생성
            history = ChatBotHistory.builder()
                    .user(user)
                    .historyFirstQuestion(summarized)
                    .historyCreatedAt(LocalDateTime.now())
                    .build();
            chatBotHistoryRepository.save(history);
        } else {
            history = chatBotHistoryRepository.findById(dto.getChatBotHistoryId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY));
        }
        // 메시지 응답
        return createMessageInternal(history, dto);
    }

    // 사용자 메시지 저장 + AI 응답 저장
    private ChatBotMessageDTO createMessageInternal(ChatBotHistory history, ChatBotRequestMessageDTO dto) {
        // 1. 사용자 메시지 저장
        ChatBotMessage userMessage = ChatBotMessage.builder()
                .messageType(dto.getMessageType()) // 보통 HUMAN
                .messageContent(dto.getMessageContent())
                .messageCreatedAt(LocalDateTime.now())
                .chatBotHistory(history)
                .build();
        chatBotMessageRepository.save(userMessage);

        // 2. AI 응답 생성
        String aiResponse = callAiApi(dto.getMessageContent());

        // 3. AI 메시지 저장
        ChatBotMessage aiMessage = ChatBotMessage.builder()
                .messageType(MessageType.AI)
                .messageContent(aiResponse)
                .messageCreatedAt(LocalDateTime.now())
                .chatBotHistory(history)
                .build();
        chatBotMessageRepository.save(aiMessage);

        // 4. 마지막 메시지 반환
        return modelMapper.map(aiMessage, ChatBotMessageDTO.class);
    }

    // FastAPI 서버로 첫 질문 요약 요청
    private String summarizeFirstQuestion(String firstQuestion) {
//        String url = aiServerUrl + "/first";
//
//        Map<String, String> request = new HashMap<>();
//        request.put("first-question", firstQuestion);
//
//        Map<String, String> response = restTemplate.postForObject(url, request, Map.class);
//        return response.get(""content"); // 예: {"result": "요약된 질문"}
//    }
       return "[요약] " + firstQuestion;
    }
    // FastAPI 서버로 일반 질문 응답 요청
    private String callAiApi(String message) {
//        String url = aiServerUrl + "/chat";
//
//        Map<String, String> request = new HashMap<>();
//        request.put("message", message);
//
//        Map<String, String> response = restTemplate.postForObject(url, request, Map.class);
//        return response.get("content"); // 예: {"result": "AI 응답"}
//    }
     return "AI 응답입니다: " + message;
    }
}
