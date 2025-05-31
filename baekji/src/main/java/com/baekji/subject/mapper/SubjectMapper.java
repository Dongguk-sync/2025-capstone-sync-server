package com.baekji.subject.mapper;

import com.baekji.subject.domain.Subject;
import com.baekji.subject.dto.SubjectDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDTO toSubjectDTO(Subject subject);
    List<SubjectDTO> toSubjectDTOList(List<Subject> subjects);
}
