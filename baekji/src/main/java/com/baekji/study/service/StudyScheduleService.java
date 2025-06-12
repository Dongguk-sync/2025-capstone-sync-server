package com.baekji.study.service;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.study.domain.StudySchedule;
import com.baekji.study.domain.Studys;
import com.baekji.study.dto.StudyScheduleDTO;
import com.baekji.study.dto.StudyScheduleRequestDTO;
import com.baekji.study.dto.StudyScheduleResponseDTO;
import com.baekji.study.dto.StudyScheduleSearchDTO;
import com.baekji.study.repository.StudyMessageRepository;
import com.baekji.study.repository.StudyScheduleRepository;
import com.baekji.study.repository.StudysRepository;
import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.domain.Subject;
import com.baekji.subject.repository.AnswerFileRepository;
import com.baekji.subject.repository.SubjectRepository;
import com.baekji.user.domain.UserEntity;
import com.baekji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;
    private final UserRepository userRepository;
    private final AnswerFileRepository answerFileRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    private final StudysRepository studysRepository;
    private final StudyMessageRepository studyMessageRepository;

    // 설명.1.1 사용자별 학습 일정 전체 조회
    public List<StudyScheduleResponseDTO> getAllStudySchedulesByUserId(Long userId) {
        List<StudySchedule> schedules = studyScheduleRepository.findAllByUser_UserId(userId);
        return schedules.stream()
                .map(this::toResponseDto) // ✅ ModelMapper 대신 직접 매핑 메서드 사용
                .collect(Collectors.toList());
    }

    // 설명.1.2 학습 일정 ID로 조회
    public StudyScheduleResponseDTO getStudyScheduleById(Long id) {
        StudySchedule schedule = studyScheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_SCHEDULE));
        return toResponseDto(schedule);
    }

    // 설명.1.3 사용자 ID와 과목명으로 학습일정 조회
    public List<StudyScheduleResponseDTO> getByUserIdAndSubjectName(Long userId, String subjectName) {
        List<StudySchedule> schedules = studyScheduleRepository
                .findByUserIdAndSubjectNameContaining(userId, subjectName);

        if (schedules.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_STUDY_SCHEDULE);
        }

        return schedules.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    //설명.1.4. 사용자 ID, 시작 날짜, 종료 날짜
    public List<StudyScheduleResponseDTO> searchSchedules(StudyScheduleSearchDTO request) {
        Long userId = request.getUserId();
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        // 필요시 JPA 메서드 or QueryDSL로 조회
        return studyScheduleRepository.findByUserIdAndDateRange(userId, start, end)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private StudyScheduleResponseDTO toResponseDto(StudySchedule schedule) {
        return StudyScheduleResponseDTO.builder()
                .studyScheduleId(schedule.getStudyScheduleId())
                .studyScheduleDate(schedule.getStudyScheduleDate())
                .studyScheduleCompleted(schedule.getStudyScheduleCompleted())
                .studyScheduleCreatedAt(schedule.getStudyScheduleCreatedAt())
                .userId(schedule.getUser().getUserId())
                .fileId(schedule.getAnswerFile().getFileId())
                .subjectId(schedule.getSubject().getSubjectId())
                .subjectName(schedule.getSubject().getSubjectName())
                .fileName(schedule.getAnswerFile().getFileName())
                .build();
    }





    //설명.2.1. 학습 일정 등록
    @Transactional
    public StudyScheduleDTO createStudySchedule(StudyScheduleRequestDTO dto) {

        // 1. 연관 엔티티 조회
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        AnswerFile answerFile = answerFileRepository.findById(dto.getFileId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));

        // 2. 엔티티 생성
        StudySchedule studySchedule = StudySchedule.builder()
                .studyScheduleDate(dto.getStudyScheduleDate())
                .studyScheduleCompleted(COMPLECTED.UNCOMP) // 기본값: 미완료
                .studyScheduleCreatedAt(LocalDateTime.now()) // 생성 시각
                .user(user)
                .answerFile(answerFile)
                .subject(subject)
                .build();

        // 3. 저장 및 응답 변환
        StudySchedule saved = studyScheduleRepository.save(studySchedule);

        //4. 학습률 갱신 (사용자)
        updateUserProgressToday(dto.getUserId());

        return modelMapper.map(saved, StudyScheduleDTO.class);


    }

    //설명.3. 학습 일정 삭제
    @Transactional
    public void deleteStudyScheduleById(Long scheduleId) {
        StudySchedule schedule = studyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STUDY_SCHEDULE));

        Long userId = schedule.getUser().getUserId();

        // 1. 일정별 학습 전체 조회
        List<Studys> studysList = studysRepository.findAllByStudySchedule_StudyScheduleId(scheduleId);

        for (Studys studys : studysList) {
            // 2. 학습별 메시지 삭제
            studyMessageRepository.deleteAllByStudys_StudysId(studys.getStudysId());
        }

        // 3. 학습 삭제
        studysRepository.deleteAll(studysList);

        // 4. 학습일정 삭제
        studyScheduleRepository.delete(schedule);

        // 5. 학습률 갱신 (사용자)
        updateUserProgressToday(userId);
    }
    
    // 설명. 학습률 갱신
    private void updateUserProgressToday(Long userId) {
        //1. 사용자 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        LocalDate today = LocalDate.now();

        //2. 오늘 날짜에 학습일정들 조회
        List<StudySchedule> todaySchedules = studyScheduleRepository
                .findAllByUser_UserIdAndStudyScheduleDate(userId, today);

        //3.학습률 갱신
        int total = todaySchedules.size();
        int completed = (int) todaySchedules.stream()
                .filter(s -> s.getStudyScheduleCompleted() == COMPLECTED.COMP)
                .count();

        user.setUserTotalStudys((long) total);
        user.setUserCompletedStudys((long) completed);
        user.setUserProgressRate(total == 0 ? 0.0 : (completed * 100.0 / total));

        userRepository.save(user);
    }

}
