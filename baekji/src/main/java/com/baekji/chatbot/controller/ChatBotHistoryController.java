package com.baekji.chatbot.controller;

import com.baekji.chatbot.dto.ChatBotHistoryDTO;
import com.baekji.chatbot.service.ChatBotHistoryService;
import com.baekji.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot-histories")
@RequiredArgsConstructor
public class ChatBotHistoryController {

    private final ChatBotHistoryService chatBotHistoryService;

    //설명.1.1. 사용자별 챗팅 히스토리 전체 조회
    // 설명.1.1. 사용자별 챗팅 히스토리 전체 조회
    @GetMapping("/user-id/{userId}")
    public ResponseDTO<List<ChatBotHistoryDTO>> getAllHistoriesByUserId(@PathVariable("userId") Long userId) {
        List<ChatBotHistoryDTO> response = chatBotHistoryService.getAllHistoriesByUserId(userId);
        return ResponseDTO.ok(response);
    }

    // 설명.1.2. 히스토리 ID로 조회
    @GetMapping("/id/{id}")
    public ResponseDTO<ChatBotHistoryDTO> getHistoryById(@PathVariable("id") Long id) {
        ChatBotHistoryDTO response = chatBotHistoryService.getHistoryById(id);
        return ResponseDTO.ok(response);
    }
}
