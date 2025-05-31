package com.baekji.study.mapper;

import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudysDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudysMapper {
    StudysDTO toStudysDTO(Studys studys);
    List<StudysDTO> toStudysDTOList(List<Studys> studysList);
}
