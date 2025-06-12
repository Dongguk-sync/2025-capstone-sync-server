package com.baekji.study.service;

import com.baekji.clova.service.STTService;
import com.baekji.common.enums.COMPLECTED;
import com.baekji.study.domain.StudySchedule;
import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.dto.StudysResponseDTO;
import com.baekji.study.repository.StudyScheduleRepository;
import com.baekji.study.repository.StudysRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.repository.AnswerFileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudysService {

    private final StudysRepository studysRepository;
    private final ModelMapper modelMapper;
    private final StudyScheduleRepository studyScheduleRepository;
    private final STTService sttService;
    private final AnswerFileRepository answerFileRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.server-url}")
    private String aiServerUrl; // ex: http://localhost:8000

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

    //설명.2. 학습하기
    @Transactional
    public StudysResponseDTO learn(Long studyScheduleId, Long answerFileId, MultipartFile speechFile) {
        // 1. 학습 일정 조회
        StudySchedule schedule = studyScheduleRepository.findById(studyScheduleId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_SCHEDULE));

        // 2. 파일 정보 조회
        AnswerFile answerFile = answerFileRepository.findById(answerFileId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE));

        // 3. 회차 계산
        Long nextRound = studysRepository.findMaxRoundByScheduleId(studyScheduleId) + 1;

        // 4. STT 처리
        String sttContent = sttService.getTextByFile(speechFile).getText();

        // 5. 파일 내용 확보 (AnswerFile의 본문 내용)
        String fileContent = answerFile.getFileContent(); // TEXT 타입이어야 함

        // 6. AI 서버 요청 구성
        Map<String, String> request = new HashMap<>();
        request.put("user_id", "user_"+String.valueOf(schedule.getUser().getUserId()));
        request.put("subject_name", schedule.getSubject().getSubjectName());
        request.put("file_name", answerFile.getFileName());
        request.put("file_content", fileContent != null ? fileContent : "");
        request.put("studys_stt_content", sttContent != null ? sttContent : "");

        // 7. AI 서버 요청
        String aiFeedback;
        try {
            Map<String, Object> response = restTemplate.postForObject(
                    aiServerUrl + "/evaluate",
                    request,
                    Map.class
            );
            if (Boolean.TRUE.equals(response.get("success"))) {
                Map<String, String> content = (Map<String, String>) response.get("content");
                aiFeedback = content.get("studys_feed_content");
            } else {
                aiFeedback = "AI 피드백 실패";
            }
        } catch (Exception e) {
            e.printStackTrace();
            aiFeedback = "AI 서버 요청 중 오류 발생";
        }

        // 8. Studys 저장
        Studys studys = Studys.builder()
                .studySchedule(schedule)
                .answerFile(answerFile)
                .studysSttContent(sttContent)
                .studysFeedContent(aiFeedback)
                .studysSubjectName(schedule.getSubject().getSubjectName())
                .studysRound(nextRound)
                .studysCreatedAt(LocalDateTime.now())
                .build();
        studysRepository.save(studys);

        // 9. 1회차면 학습 완료 처리
        if (nextRound == 1) {
            schedule.setStudyScheduleCompleted(COMPLECTED.COMP);
        }

        // 10. 응답 DTO 반환
        return modelMapper.map(studys, StudysResponseDTO.class);
    }


}
