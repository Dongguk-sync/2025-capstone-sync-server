package com.baekji.study.mapper;

import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudysDTO;
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
public class StudysMapperImpl implements StudysMapper {

    @Override
    public StudysDTO toStudysDTO(Studys studys) {
        if ( studys == null ) {
            return null;
        }

        StudysDTO.StudysDTOBuilder studysDTO = StudysDTO.builder();

        studysDTO.studysId( studys.getStudysId() );
        studysDTO.studysSttContent( studys.getStudysSttContent() );
        studysDTO.studysFeedContent( studys.getStudysFeedContent() );
        studysDTO.studysSubjectName( studys.getStudysSubjectName() );
        studysDTO.studysCreatedAt( studys.getStudysCreatedAt() );
        if ( studys.getStudysRound() != null ) {
            studysDTO.studysRound( studys.getStudysRound().intValue() );
        }

        return studysDTO.build();
    }

    @Override
    public List<StudysDTO> toStudysDTOList(List<Studys> studysList) {
        if ( studysList == null ) {
            return null;
        }

        List<StudysDTO> list = new ArrayList<StudysDTO>( studysList.size() );
        for ( Studys studys : studysList ) {
            list.add( toStudysDTO( studys ) );
        }

        return list;
    }
}
