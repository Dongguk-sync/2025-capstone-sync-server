package com.baekji.clova.service;

import com.baekji.clova.domain.NaverCloudClient;
import com.baekji.clova.dto.STTResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class STTService {
    private final NaverCloudClient naverCloudClient;

    public STTResponse getTextByFile(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String response = naverCloudClient.soundToText(fileBytes);
            return new STTResponse(response);
        } catch (Exception e) {
            throw new InvalidFileNameException("잘못된 파일", null);
        }
    }
}