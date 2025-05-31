package com.baekji.study.service;

import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.repository.StudysRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudysService {

    private final StudysRepository studysRepository;
    private final ModelMapper modelMapper;

    public List<StudysDTO> getAllStudys() {
        List<Studys> studysList = studysRepository.findAll();
        if (studysList.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_STUDYS);
        }
        return studysList.stream()
                .map(studys -> modelMapper.map(studys, StudysDTO.class))
                .collect(Collectors.toList());
    }

    public StudysDTO getStudysById(Long id) {
        Studys studys = studysRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDYS));
        return modelMapper.map(studys, StudysDTO.class);
    }
}
