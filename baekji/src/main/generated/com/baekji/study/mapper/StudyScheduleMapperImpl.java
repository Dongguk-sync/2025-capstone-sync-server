package com.baekji.study.mapper;

import com.baekji.study.domain.StudySchedule;
import com.baekji.study.dto.StudyScheduleDTO;
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
public class StudyScheduleMapperImpl implements StudyScheduleMapper {

    @Override
    public StudyScheduleDTO toStudyScheduleDTO(StudySchedule studySchedule) {
        if ( studySchedule == null ) {
            return null;
        }

        StudyScheduleDTO.StudyScheduleDTOBuilder studyScheduleDTO = StudyScheduleDTO.builder();

        studyScheduleDTO.studyScheduleId( studySchedule.getStudyScheduleId() );
        studyScheduleDTO.studyScheduleDate( studySchedule.getStudyScheduleDate() );
        studyScheduleDTO.studyScheduleCompleted( studySchedule.getStudyScheduleCompleted() );
        studyScheduleDTO.studyScheduleCreatedAt( studySchedule.getStudyScheduleCreatedAt() );

        return studyScheduleDTO.build();
    }

    @Override
    public List<StudyScheduleDTO> toStudyScheduleDTOList(List<StudySchedule> studySchedules) {
        if ( studySchedules == null ) {
            return null;
        }

        List<StudyScheduleDTO> list = new ArrayList<StudyScheduleDTO>( studySchedules.size() );
        for ( StudySchedule studySchedule : studySchedules ) {
            list.add( toStudyScheduleDTO( studySchedule ) );
        }

        return list;
    }
}
