package com.baekji.chatbot.dto;

import lombok.Data;

@Data
public class ChatBotAiResponseDTO {
    private boolean success;
    private ContentDTO content;

    @Data
    public static class ContentDTO {
        private String message_type;
        private String message_content;
        private String message_created_at;
        private String subject_name;
        private String file_name;
        private String file_url;
        private Object[] related_chunks; // 필요하면 타입 지정
    }
}
