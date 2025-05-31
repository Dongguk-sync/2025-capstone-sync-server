package com.baekji.subject.mapper;

import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.dto.AnswerFileDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerFileMapper {
    AnswerFileDTO toAnswerFileDTO(AnswerFile answerFile);
    List<AnswerFileDTO> toAnswerFileDTOList(List<AnswerFile> answerFiles);
}
