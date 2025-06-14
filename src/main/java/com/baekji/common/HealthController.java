package com.baekji.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final Environment env;

    @GetMapping
    public String healthCheck() {
        return "헬스 체크 OK - 현재 포트: " + env.getProperty("local.server.port");
    }
}
