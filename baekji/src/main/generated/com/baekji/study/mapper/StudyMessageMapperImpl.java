package com.baekji.study.mapper;

import com.baekji.study.domain.StudyMessage;
import com.baekji.study.dto.StudyMessageDTO;
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
public class StudyMessageMapperImpl implements StudyMessageMapper {

    @Override
    public StudyMessageDTO toStudyMessageDTO(StudyMessage studyMessage) {
        if ( studyMessage == null ) {
            return null;
        }

        StudyMessageDTO.StudyMessageDTOBuilder studyMessageDTO = StudyMessageDTO.builder();

        studyMessageDTO.smId( studyMessage.getSmId() );
        studyMessageDTO.messageType( studyMessage.getMessageType() );
        studyMessageDTO.smContent( studyMessage.getSmContent() );
        studyMessageDTO.smSubjectName( studyMessage.getSmSubjectName() );
        studyMessageDTO.smFileName( studyMessage.getSmFileName() );
        studyMessageDTO.smFileUrl( studyMessage.getSmFileUrl() );
        studyMessageDTO.smCreatedAt( studyMessage.getSmCreatedAt() );

        return studyMessageDTO.build();
    }

    @Override
    public List<StudyMessageDTO> toStudyMessageDTOList(List<StudyMessage> studyMessages) {
        if ( studyMessages == null ) {
            return null;
        }

        List<StudyMessageDTO> list = new ArrayList<StudyMessageDTO>( studyMessages.size() );
        for ( StudyMessage studyMessage : studyMessages ) {
            list.add( toStudyMessageDTO( studyMessage ) );
        }

        return list;
    }
}
