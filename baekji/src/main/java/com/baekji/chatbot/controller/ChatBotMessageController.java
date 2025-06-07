package com.baekji.chatbot.controller;

import com.baekji.chatbot.dto.ChatBotMessageDTO;
import com.baekji.chatbot.service.ChatBotMessageService;
import com.baekji.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot-messages")
@RequiredArgsConstructor
public class ChatBotMessageController {

    private final ChatBotMessageService chatBotMessageService;

    //설명.1.1. 히스토리별 메시지 리스트 조회
    @GetMapping("/chat_bot_history_id/{historyId}")
    public ResponseDTO<List<ChatBotMessageDTO>> getMessagesByHistoryId(@PathVariable("historyId") Long historyId) {
        List<ChatBotMessageDTO> response = chatBotMessageService.getMessagesByHistoryId(historyId);
        return ResponseDTO.ok(response);
    }

}
