package com.baekji.subject.service;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.study.repository.StudyScheduleRepository;
import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.domain.Subject;
import com.baekji.study.domain.StudySchedule;
import com.baekji.subject.dto.AIFTTRequestDTO;
import com.baekji.subject.dto.AIFTTResponseDTO;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.dto.AnswerResponseFileDTO;
import com.baekji.subject.repository.AnswerFileRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


// ★ PDF, DOC, DOCX 관련 import 추가
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
@Service
@RequiredArgsConstructor
public class AnswerFileService {

    private final AnswerFileRepository answerFileRepository;
    private final SubjectRepository subjectRepository; // Subject 조회를 위해 필요
    private final ModelMapper modelMapper;
    private final StudyScheduleRepository studyScheduleRepository; // DI 주입 필요
    private final RestTemplate restTemplate;

    @Value("${ai.server-url}")
    private String aiServerUrl; // ex: http://localhost:8000

    // 설명.1.1. 과목별 교안 전체 조회
    public List<AnswerResponseFileDTO> getAnswerFilesBySubjectId(Long subjectId) {
        List<AnswerFile> answerFiles = answerFileRepository.findBySubjectSubjectId(subjectId);
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFiles.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 설명.1.2. 교안 id로 조회
    public AnswerResponseFileDTO getAnswerFileById(Long id) {
        AnswerFile answerFile = answerFileRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE));
        return toResponseDTO(answerFile);
    }

    // 설명.1.3. 사용자별 교안 전체 조회
    public List<AnswerResponseFileDTO> getAnswerFilesByUserId(Long userId) {
        List<AnswerFile> answerFiles = answerFileRepository.findBySubjectUserUserId(userId);
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFiles.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 설명.1.4. 사용자별 교안명으로 조회
    public List<AnswerResponseFileDTO> getAnswerFilesByUserIdAndFileName(Long userId, String fileName) {
        List<AnswerFile> answerFiles = answerFileRepository.findBySubjectUserUserIdAndFileNameContaining(userId, fileName);
        if (answerFiles.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE);
        }
        return answerFiles.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    // 설명.2. 교안 등록
    @Transactional
    public AnswerFileDTO saveAnswerFile(Long userId, Long subjectId, MultipartFile file) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));

        // 파일명 및 타입 추출
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new CommonException(ErrorCode.UNSUPPORTED_FILE_FORMAT);
        }

        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String fileType = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        // 파일 내용 추출
        String fileContent = extractFileContent(fileType, file);

        // 1차 저장 (fileUrl 없이)
        AnswerFile answerFile = AnswerFile.builder()
                .fileName(fileName)
                .fileType(fileType)
                .fileContent(fileContent)
                .createdAt(LocalDateTime.now())
                .subject(subject)
                .build();

        AnswerFile savedFile = answerFileRepository.save(answerFile);

        // fileUrl 설정 후 AI 서버로 전송
        String fileUrl = "/subjects/" + subjectId + "/files/" + savedFile.getFileId();
        savedFile.setFileUrl(fileUrl);

        // AI 서버 통신 요청 DTO 구성
        AIFTTRequestDTO aiRequest = new AIFTTRequestDTO(
                "user_"+ String.valueOf(userId),  // "user_1", "user_2" 형태
                subject.getSubjectName(),
                fileName,
                fileContent,
                fileUrl
        );

        // AI 서버 POST 요청
        try {
            String url = aiServerUrl + "/preprocess/ftt";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AIFTTRequestDTO> entity = new HttpEntity<>(aiRequest, headers);

            ResponseEntity<AIFTTResponseDTO> response =
                    restTemplate.postForEntity(url, entity, AIFTTResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                savedFile.setFileContent(response.getBody().getContent());
            }
        } catch (Exception e) {
            System.out.println("AI 서버 통신 실패: " + e.getMessage());
        }

        // 최종 저장 및 반환
        AnswerFile updatedFile = answerFileRepository.save(savedFile);
        return modelMapper.map(updatedFile, AnswerFileDTO.class);
    }

    // 파일 추출 분기 메서드 분리
    private String extractFileContent(String fileType, MultipartFile file) {
        try {
            switch (fileType.toLowerCase()) {
                case "pdf": return extractTextFromPDF(file);
                case "docx": return extractTextFromDocx(file);
                case "doc": return extractTextFromDoc(file);
                default: return extractTextFromTextFile(file);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.UNSUPPORTED_FILE_FORMAT);
        }
    }

    // PDF 텍스트 추출
    public String extractTextFromPDF(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // DOCX 텍스트 추출
    public String extractTextFromDocx(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        }
    }

    // DOC 텍스트 추출
    public String extractTextFromDoc(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             HWPFDocument document = new HWPFDocument(inputStream)) {

            WordExtractor extractor = new WordExtractor(document);
            return extractor.getText();
        }
    }

    // 텍스트 파일(txt) 읽기
    public String extractTextFromTextFile(MultipartFile file) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }


    @Transactional
    public void deleteAnswerFile(Long fileId) {
        AnswerFile answerFile = answerFileRepository.findById(fileId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_ANSWER_FILE));

        answerFileRepository.delete(answerFile);
    }

    // 파일 -> 응답 DTO 변환
    private AnswerResponseFileDTO toResponseDTO(AnswerFile file) {
        LocalDate recentStudiedDate = studyScheduleRepository
                .findTopByAnswerFileOrderByStudyScheduleDateDesc(file) // 상태 조건 없이 가장 최근 일정
                .filter(schedule -> schedule.getStudyScheduleCompleted() == COMPLECTED.COMP) // 상태 검사
                .map(StudySchedule::getStudyScheduleDate)
                .orElse(null);

        return AnswerResponseFileDTO.builder()
                .fileId(file.getFileId())
                .fileName(file.getFileName())
                .fileContent(file.getFileContent())
                .fileType(file.getFileType())
                .fileUrl(file.getFileUrl())
                .createdAt(file.getCreatedAt())
                .subjectId(file.getSubject().getSubjectId())
                .recentStudiedDate(recentStudiedDate)
                .build();
    }


}
