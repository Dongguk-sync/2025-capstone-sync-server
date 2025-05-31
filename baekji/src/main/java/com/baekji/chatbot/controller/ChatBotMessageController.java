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

    @GetMapping
    public ResponseDTO<List<ChatBotMessageDTO>> getAllMessages() {
        List<ChatBotMessageDTO> response = chatBotMessageService.getAllMessages();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<ChatBotMessageDTO> getMessageById(@PathVariable Long id) {
        ChatBotMessageDTO response = chatBotMessageService.getMessageById(id);
        return ResponseDTO.ok(response);
    }
}
