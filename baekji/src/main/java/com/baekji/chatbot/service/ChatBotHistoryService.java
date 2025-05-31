package com.baekji.chatbot.service;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.dto.ChatBotHistoryDTO;
import com.baekji.chatbot.mapper.ChatBotHistoryMapper;
import com.baekji.chatbot.repository.ChatBotHistoryRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotHistoryService {

    private final ChatBotHistoryRepository chatBotHistoryRepository;
    private final ChatBotHistoryMapper chatBotHistoryMapper;

    public List<ChatBotHistoryDTO> getAllHistories() {
        List<ChatBotHistory> histories = chatBotHistoryRepository.findAll();
        if (histories.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY);
        }
        return chatBotHistoryMapper.toChatBotHistoryDTOList(histories);
    }

    public ChatBotHistoryDTO getHistoryById(Long id) {
        ChatBotHistory history = chatBotHistoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY));
        return chatBotHistoryMapper.toChatBotHistoryDTO(history);
    }
}
