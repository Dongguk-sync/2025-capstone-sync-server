package com.baekji.study.dto;

import com.baekji.chatbot.dto.AILectureMessageDTO;
import com.baekji.chatbot.dto.ChatBotMessageDTO;
import com.baekji.study.domain.StudyMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AIServerRequestDTO {

    @JsonProperty("question")
    private String question;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_content")
    private String fileContent;

    @JsonProperty("studys_stt_content")
    private String studysSttContent;

    @JsonProperty("studys_feed_content")
    private String studysFeedContent;

    @JsonProperty("chat_bot_history_id")
    private String chatBotHistoryId;

    @JsonProperty("chat_bot_history")
    private List<AILectureMessageDTO> chatBotHistory;
}
