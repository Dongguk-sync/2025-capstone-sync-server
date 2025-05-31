package com.baekji.chatbot.mapper;

import com.baekji.chatbot.domain.ChatBotHistory;
import com.baekji.chatbot.dto.ChatBotHistoryDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-31T23:09:53+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ChatBotHistoryMapperImpl implements ChatBotHistoryMapper {

    @Override
    public ChatBotHistoryDTO toChatBotHistoryDTO(ChatBotHistory chatBotHistory) {
        if ( chatBotHistory == null ) {
            return null;
        }

        ChatBotHistoryDTO.ChatBotHistoryDTOBuilder chatBotHistoryDTO = ChatBotHistoryDTO.builder();

        chatBotHistoryDTO.chatBotHistoryId( chatBotHistory.getChatBotHistoryId() );
        chatBotHistoryDTO.historyFirstQuestion( chatBotHistory.getHistoryFirstQuestion() );
        chatBotHistoryDTO.historyCreatedAt( chatBotHistory.getHistoryCreatedAt() );

        return chatBotHistoryDTO.build();
    }

    @Override
    public List<ChatBotHistoryDTO> toChatBotHistoryDTOList(List<ChatBotHistory> chatBotHistories) {
        if ( chatBotHistories == null ) {
            return null;
        }

        List<ChatBotHistoryDTO> list = new ArrayList<ChatBotHistoryDTO>( chatBotHistories.size() );
        for ( ChatBotHistory chatBotHistory : chatBotHistories ) {
            list.add( toChatBotHistoryDTO( chatBotHistory ) );
        }

        return list;
    }
}
