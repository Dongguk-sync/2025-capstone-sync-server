package com.baekji.chatbot.mapper;

import com.baekji.chatbot.domain.ChatBotMessage;
import com.baekji.chatbot.dto.ChatBotMessageDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatBotMessageMapper {
    ChatBotMessageDTO toChatBotMessageDTO(ChatBotMessage chatBotMessage);
    List<ChatBotMessageDTO> toChatBotMessageDTOList(List<ChatBotMessage> chatBotMessages);
}
