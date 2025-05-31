package com.baekji.chatbot.repository;

import com.baekji.chatbot.domain.ChatBotMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotMessageRepository extends JpaRepository<ChatBotMessage, Long> {
}
