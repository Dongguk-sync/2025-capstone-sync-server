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

    @GetMapping
    public ResponseDTO<List<ChatBotHistoryDTO>> getAllHistories() {
        List<ChatBotHistoryDTO> response = chatBotHistoryService.getAllHistories();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<ChatBotHistoryDTO> getHistoryById(@PathVariable Long id) {
        ChatBotHistoryDTO response = chatBotHistoryService.getHistoryById(id);
        return ResponseDTO.ok(response);
    }
}
