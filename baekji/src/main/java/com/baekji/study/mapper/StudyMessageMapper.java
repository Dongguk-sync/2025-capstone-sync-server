package com.baekji.study.mapper;

import com.baekji.study.domain.StudyMessage;
import com.baekji.study.dto.StudyMessageDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudyMessageMapper {
    StudyMessageDTO toStudyMessageDTO(StudyMessage studyMessage);
    List<StudyMessageDTO> toStudyMessageDTOList(List<StudyMessage> studyMessages);
}
