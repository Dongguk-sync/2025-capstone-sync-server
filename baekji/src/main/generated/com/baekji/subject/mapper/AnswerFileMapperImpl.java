package com.baekji.subject.mapper;

import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.dto.AnswerFileDTO;
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
public class AnswerFileMapperImpl implements AnswerFileMapper {

    @Override
    public AnswerFileDTO toAnswerFileDTO(AnswerFile answerFile) {
        if ( answerFile == null ) {
            return null;
        }

        AnswerFileDTO.AnswerFileDTOBuilder answerFileDTO = AnswerFileDTO.builder();

        answerFileDTO.fileId( answerFile.getFileId() );
        answerFileDTO.fileName( answerFile.getFileName() );
        answerFileDTO.fileContent( answerFile.getFileContent() );
        answerFileDTO.fileType( answerFile.getFileType() );
        answerFileDTO.fileUrl( answerFile.getFileUrl() );
        answerFileDTO.createdAt( answerFile.getCreatedAt() );

        return answerFileDTO.build();
    }

    @Override
    public List<AnswerFileDTO> toAnswerFileDTOList(List<AnswerFile> answerFiles) {
        if ( answerFiles == null ) {
            return null;
        }

        List<AnswerFileDTO> list = new ArrayList<AnswerFileDTO>( answerFiles.size() );
        for ( AnswerFile answerFile : answerFiles ) {
            list.add( toAnswerFileDTO( answerFile ) );
        }

        return list;
    }
}
