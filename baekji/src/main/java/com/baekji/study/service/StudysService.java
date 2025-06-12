package com.baekji.study.service;

import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.dto.StudysResponseDTO;
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

    //설명.1.1. id로 조회
    public StudysDTO getStudysById(Long id) {
        Studys studys = studysRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDYS));
        return modelMapper.map(studys, StudysDTO.class);
    }

    //설명.1.2. 스케줄일정 id로 조회
    public List<StudysResponseDTO> getAllStudysByScheduleId(Long scheduleId) {
        List<Studys> list = studysRepository.findAllByStudySchedule_StudyScheduleId(scheduleId);
        return list.stream()
                .map(study -> modelMapper.map(study, StudysResponseDTO.class))
                .collect(Collectors.toList());
    }

    //설명.1.3. 교안별 전체 학습 조회
    public List<StudysResponseDTO> getAllStudysByFileId(Long fileId) {
        List<Studys> list = studysRepository.findAllByAnswerFile_FileId(fileId);
        return list.stream()
                .map(study -> modelMapper.map(study, StudysResponseDTO.class))
                .collect(Collectors.toList());
    }
}
