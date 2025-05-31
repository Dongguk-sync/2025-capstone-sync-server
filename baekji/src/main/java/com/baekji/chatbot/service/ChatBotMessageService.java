package com.baekji.chatbot.service;

import com.baekji.chatbot.domain.ChatBotMessage;
import com.baekji.chatbot.dto.ChatBotMessageDTO;
import com.baekji.chatbot.repository.ChatBotMessageRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBotMessageService {

    private final ChatBotMessageRepository chatBotMessageRepository;
    private final ModelMapper modelMapper;

    public List<ChatBotMessageDTO> getAllMessages() {
        List<ChatBotMessage> messages = chatBotMessageRepository.findAll();
        if (messages.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_CHATBOT_MESSAGE);
        }
        return messages.stream()
                .map(message -> modelMapper.map(message, ChatBotMessageDTO.class))
                .collect(Collectors.toList());
    }

    public ChatBotMessageDTO getMessageById(Long id) {
        ChatBotMessage message = chatBotMessageRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHATBOT_MESSAGE));
        return modelMapper.map(message, ChatBotMessageDTO.class);
    }
}
