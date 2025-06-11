package com.baekji.subject.service;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.study.repository.StudyScheduleRepository;
import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.domain.Subject;
import com.baekji.study.domain.StudySchedule;
import com.baekji.subject.dto.AnswerFileDTO;
import com.baekji.subject.dto.AnswerResponseFileDTO;
import com.baekji.subject.repository.AnswerFileRepository;
import com.baekji.common.exception.CommonException;
import com.baekji.common.exception.ErrorCode;
import com.baekji.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public AnswerFileDTO saveAnswerFile(Long subjectId, MultipartFile file) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_SUBJECT));

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new CommonException(ErrorCode.UNSUPPORTED_FILE_FORMAT);
        }

        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String fileType = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        String fileContent = "";

        try {
            if ("pdf".equalsIgnoreCase(fileType)) {
                fileContent = extractTextFromPDF(file);
            } else if ("docx".equalsIgnoreCase(fileType)) {
                fileContent = extractTextFromDocx(file);
            } else if ("doc".equalsIgnoreCase(fileType)) {
                fileContent = extractTextFromDoc(file);
            } else {
                fileContent = extractTextFromTextFile(file);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.UNSUPPORTED_FILE_FORMAT);
        }

        // 1차 저장 (fileUrl 없이)
        AnswerFile answerFile = AnswerFile.builder()
                .fileName(fileName)
                .fileType(fileType)
                .fileContent(fileContent)
                .createdAt(LocalDateTime.now())
                .subject(subject)
                .build();

        AnswerFile savedFile = answerFileRepository.save(answerFile);

        // fileId 기반으로 fileUrl 세팅
        String fileUrl = "/subjects/" + subjectId + "/files/" + savedFile.getFileId();
        savedFile.setFileUrl(fileUrl);

        // 다시 저장
        AnswerFile updatedFile = answerFileRepository.save(savedFile);

        return modelMapper.map(updatedFile, AnswerFileDTO.class);
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
