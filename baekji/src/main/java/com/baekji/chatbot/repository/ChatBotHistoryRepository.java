package com.baekji.chatbot.repository;

import com.baekji.chatbot.domain.ChatBotHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotHistoryRepository extends JpaRepository<ChatBotHistory, Long> {
}
