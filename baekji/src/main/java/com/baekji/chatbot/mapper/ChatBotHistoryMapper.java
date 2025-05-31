package com.baekji.chatbot.mapper;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.dto.ChatBotHistoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatBotHistoryMapper {
    ChatBotHistoryDTO toChatBotHistoryDTO(ChatBotHistory chatBotHistory);
    List<ChatBotHistoryDTO> toChatBotHistoryDTOList(List<ChatBotHistory> chatBotHistories);
}
