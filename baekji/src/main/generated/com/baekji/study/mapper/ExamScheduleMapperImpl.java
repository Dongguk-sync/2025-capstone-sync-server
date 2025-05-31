package com.baekji.study.mapper;

import com.baekji.study.domain.ExamSchedule;
import com.baekji.study.dto.ExamScheduleDTO;
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
public class ExamScheduleMapperImpl implements ExamScheduleMapper {

    @Override
    public ExamScheduleDTO toExamScheduleDTO(ExamSchedule examSchedule) {
        if ( examSchedule == null ) {
            return null;
        }

        ExamScheduleDTO.ExamScheduleDTOBuilder examScheduleDTO = ExamScheduleDTO.builder();

        examScheduleDTO.examScheduleId( examSchedule.getExamScheduleId() );
        examScheduleDTO.examScheduleDate( examSchedule.getExamScheduleDate() );
        examScheduleDTO.examScheduleName( examSchedule.getExamScheduleName() );

        return examScheduleDTO.build();
    }

    @Override
    public List<ExamScheduleDTO> toExamScheduleDTOList(List<ExamSchedule> examSchedules) {
        if ( examSchedules == null ) {
            return null;
        }

        List<ExamScheduleDTO> list = new ArrayList<ExamScheduleDTO>( examSchedules.size() );
        for ( ExamSchedule examSchedule : examSchedules ) {
            list.add( toExamScheduleDTO( examSchedule ) );
        }

        return list;
    }
}
