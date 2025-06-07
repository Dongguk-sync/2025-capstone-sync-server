package com.baekji.chatbot.repository;

import com.baekji.chatbot.domain.ChatBotMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatBotMessageRepository extends JpaRepository<ChatBotMessage, Long> {
    List<ChatBotMessage> findByChatBotHistoryChatBotHistoryId(Long chatBotHistoryId);
}
