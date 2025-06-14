package com.baekji.chatbot.repository;

import com.baekji.chatbot.domain.ChatBotHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatBotHistoryRepository extends JpaRepository<ChatBotHistory, Long> {

    // 설명.1.1. 사용자 ID로 챗팅 히스토리 목록 조회
    List<ChatBotHistory> findByUser_UserId(Long userId);
}
