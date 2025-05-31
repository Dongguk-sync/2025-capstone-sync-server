package com.baekji.chatbot.mapper;

import com.baekji.chatbot.domain.ChatBotMessage;
import com.baekji.chatbot.dto.ChatBotMessageDTO;
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
public class ChatBotMessageMapperImpl implements ChatBotMessageMapper {

    @Override
    public ChatBotMessageDTO toChatBotMessageDTO(ChatBotMessage chatBotMessage) {
        if ( chatBotMessage == null ) {
            return null;
        }

        ChatBotMessageDTO.ChatBotMessageDTOBuilder chatBotMessageDTO = ChatBotMessageDTO.builder();

        chatBotMessageDTO.botMessageId( chatBotMessage.getBotMessageId() );
        chatBotMessageDTO.messageType( chatBotMessage.getMessageType() );
        chatBotMessageDTO.messageContent( chatBotMessage.getMessageContent() );
        chatBotMessageDTO.messageCreatedAt( chatBotMessage.getMessageCreatedAt() );
        chatBotMessageDTO.subjectName( chatBotMessage.getSubjectName() );
        chatBotMessageDTO.fileName( chatBotMessage.getFileName() );
        chatBotMessageDTO.fileUrl( chatBotMessage.getFileUrl() );

        return chatBotMessageDTO.build();
    }

    @Override
    public List<ChatBotMessageDTO> toChatBotMessageDTOList(List<ChatBotMessage> chatBotMessages) {
        if ( chatBotMessages == null ) {
            return null;
        }

        List<ChatBotMessageDTO> list = new ArrayList<ChatBotMessageDTO>( chatBotMessages.size() );
        for ( ChatBotMessage chatBotMessage : chatBotMessages ) {
            list.add( toChatBotMessageDTO( chatBotMessage ) );
        }

        return list;
    }
}
