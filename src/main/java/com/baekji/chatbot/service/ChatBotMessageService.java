package com.baekji.chatbot.service;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.domain.ChatBotMessage;
import com.baekji.chatbot.dto.ChatBotAiResponseDTO;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        //필기.1. history_id가 null일때 질문한 경우 히스토리 생성
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
                    .historyCreatedAt(LocalDateTime.now().withNano(0))
                    .build();
            chatBotHistoryRepository.save(history);
        } else {
            history = chatBotHistoryRepository.findById(dto.getChatBotHistoryId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY));
        }

        //필기.2. history_id가 null일때 질문한 경우 히스토리 생성
        // 메시지 응답
        return createMessageInternal(history, dto);
    }

    // 사용자 메시지 저장 + AI 응답 저장
    private ChatBotMessageDTO createMessageInternal(ChatBotHistory history, ChatBotRequestMessageDTO dto) {
        // 1. 사용자 메시지 저장
        ChatBotMessage userMessage = ChatBotMessage.builder()
                .messageType(dto.getMessageType()) // 보통 HUMAN
                .messageContent(dto.getMessageContent())
                .messageCreatedAt(LocalDateTime.now().withNano(0))
                .chatBotHistory(history)
                .build();
        chatBotMessageRepository.save(userMessage);

        // 2. AI 응답 생성
        ChatBotAiResponseDTO.ContentDTO aiContent = callAiApi(history, dto);

        if (aiContent == null) {
            throw new CommonException(ErrorCode.AI_RESPONSE_FAILED);
        }

        // 3. AI 메시지 저장 (여기에 모든 필드 반영)
        ChatBotMessage aiMessage = ChatBotMessage.builder()
                .messageType(MessageType.AI)
                .messageContent(aiContent.getMessage_content())
                .messageCreatedAt(LocalDateTime.now().withNano(0))
                .chatBotHistory(history)
                .subjectName(aiContent.getSubject_name())
                .fileName(aiContent.getFile_name())
                .fileUrl(aiContent.getFile_url())
                .build();
        chatBotMessageRepository.save(aiMessage);

        return modelMapper.map(aiMessage, ChatBotMessageDTO.class);
    }

    // FastAPI 서버로 첫 질문 요약 요청
    private String summarizeFirstQuestion(String firstQuestion) {
        String url = aiServerUrl + "/chat/summary_session";

        Map<String, String> request = new HashMap<>();
        request.put("first_question", firstQuestion);

        Map<String, String> response = restTemplate.postForObject(url, request, Map.class);
        return response.get("summary"); // 예: {"result": "요약된 질문"}
    }

    // FastAPI 서버로 일반 질문 응답 요청
    private ChatBotAiResponseDTO.ContentDTO callAiApi(ChatBotHistory history, ChatBotRequestMessageDTO dto) {
        String url = aiServerUrl + "/chat/chatbot";

        Map<String, Object> request = new HashMap<>();
        request.put("question", dto.getMessageContent());
        request.put("user_id", "user_" + dto.getUserId());
        request.put("chat_bot_history_id", "history_" + history.getChatBotHistoryId());

        List<ChatBotMessage> messageList =
                chatBotMessageRepository.findByChatBotHistoryChatBotHistoryId(history.getChatBotHistoryId());

        List<Map<String, String>> chatHistory = messageList.stream()
                .map(msg -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("message_type", msg.getMessageType().toString());
                    m.put("message_content", msg.getMessageContent());
                    return m;
                })
                .collect(Collectors.toList());

        request.put("chat_bot_history", chatHistory);

        try {
            ChatBotAiResponseDTO response = restTemplate.postForObject(url, request, ChatBotAiResponseDTO.class);

            log.info("AI 서버 응답: {}", response);

            if (response != null && response.isSuccess()) {
                return response.getContent(); // ✅ 전체 ContentDTO 반환
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("AI 서버 통신 중 예외", e);
            return null;
        }

    }


}
