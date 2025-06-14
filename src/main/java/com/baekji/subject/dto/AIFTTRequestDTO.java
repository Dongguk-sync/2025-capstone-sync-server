package com.baekji.subject.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIFTTRequestDTO {
    private String user_id;
    private String subject_name;
    private String file_name;
    private String file_content;
    private String file_url;
}
