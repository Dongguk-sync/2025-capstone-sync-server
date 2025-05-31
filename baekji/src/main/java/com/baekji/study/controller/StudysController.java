package com.baekji.study.controller;

import com.baekji.common.ResponseDTO;
import com.baekji.study.dto.StudysDTO;
import com.baekji.study.service.StudysService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studys")
@RequiredArgsConstructor
public class StudysController {

    private final StudysService studysService;

    @GetMapping
    public ResponseDTO<List<StudysDTO>> getAllStudys() {
        List<StudysDTO> response = studysService.getAllStudys();
        return ResponseDTO.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseDTO<StudysDTO> getStudysById(@PathVariable Long id) {
        StudysDTO response = studysService.getStudysById(id);
        return ResponseDTO.ok(response);
    }
}
