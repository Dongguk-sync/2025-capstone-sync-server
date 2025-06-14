package com.baekji.chatbot.service;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.dto.ChatBotHistoryDTO;
import com.baekji.chatbot.repository.ChatBotHistoryRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBotHistoryService {

    private final ChatBotHistoryRepository chatBotHistoryRepository;
    private final ModelMapper modelMapper;

    // 설명.1.1. 사용자 ID로 챗팅 히스토리 전체 조회
    public List<ChatBotHistoryDTO> getAllHistoriesByUserId(Long userId) {
        List<ChatBotHistory> histories = chatBotHistoryRepository.findByUser_UserId(userId);
        if (histories.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY);
        }
        return histories.stream()
                .map(history -> modelMapper.map(history, ChatBotHistoryDTO.class))
                .collect(Collectors.toList());
    }

    // 설명.1.2. 챗팅 히스토리 ID로 단건 조회
    public ChatBotHistoryDTO getHistoryById(Long id) {
        ChatBotHistory history = chatBotHistoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY));
        return modelMapper.map(history, ChatBotHistoryDTO.class);
    }
}
