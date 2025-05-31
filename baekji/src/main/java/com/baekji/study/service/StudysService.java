package com.baekji.study.service;

import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.mapper.StudysMapper;
import com.baekji.study.repository.StudysRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudysService {

    private final StudysRepository studysRepository;
    private final StudysMapper studysMapper;

    public List<StudysDTO> getAllStudys() {
        List<Studys> studysList = studysRepository.findAll();
        if (studysList.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_STUDYS);
        }
        return studysMapper.toStudysDTOList(studysList);
    }

    public StudysDTO getStudysById(Long id) {
        Studys studys = studysRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDYS));
        return studysMapper.toStudysDTO(studys);
    }
}
