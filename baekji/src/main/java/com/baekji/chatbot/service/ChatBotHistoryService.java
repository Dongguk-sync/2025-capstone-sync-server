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

    public List<ChatBotHistoryDTO> getAllHistories() {
        List<ChatBotHistory> histories = chatBotHistoryRepository.findAll();
        if (histories.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY);
        }
        return histories.stream()
                .map(history -> modelMapper.map(history, ChatBotHistoryDTO.class))
                .collect(Collectors.toList());
    }

    public ChatBotHistoryDTO getHistoryById(Long id) {
        ChatBotHistory history = chatBotHistoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATBOT_HISTORY));
        return modelMapper.map(history, ChatBotHistoryDTO.class);
    }
}
