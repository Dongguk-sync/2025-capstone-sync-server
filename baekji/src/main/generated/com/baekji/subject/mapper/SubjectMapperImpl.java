package com.baekji.subject.mapper;

import com.baekji.subject.domain.Subject;
import com.baekji.subject.dto.SubjectDTO;
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
public class SubjectMapperImpl implements SubjectMapper {

    @Override
    public SubjectDTO toSubjectDTO(Subject subject) {
        if ( subject == null ) {
            return null;
        }

        SubjectDTO.SubjectDTOBuilder subjectDTO = SubjectDTO.builder();

        subjectDTO.subjectId( subject.getSubjectId() );
        subjectDTO.subjectName( subject.getSubjectName() );
        subjectDTO.createdAt( subject.getCreatedAt() );

        return subjectDTO.build();
    }

    @Override
    public List<SubjectDTO> toSubjectDTOList(List<Subject> subjects) {
        if ( subjects == null ) {
            return null;
        }

        List<SubjectDTO> list = new ArrayList<SubjectDTO>( subjects.size() );
        for ( Subject subject : subjects ) {
            list.add( toSubjectDTO( subject ) );
        }

        return list;
    }
}
