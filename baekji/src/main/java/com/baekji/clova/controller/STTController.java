package com.baekji.clova.controller;

import com.baekji.clova.dto.STTResponse;
import com.baekji.clova.service.STTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class STTController {
    private final STTService tmpService;

    @PostMapping("/api/stt")
    public ResponseEntity<STTResponse> getTextByFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(tmpService.getTextByFile(file));
    }
}