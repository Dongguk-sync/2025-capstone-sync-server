package com.baekji.study.domain;

import com.baekji.common.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType; // AI, HUMAN

    @Column(nullable = false, columnDefinition = "TEXT")
    private String smContent;

    @Column(nullable = false)
    private String smSubjectName;

    @Column(nullable = false)
    private String smFileName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String smFileUrl;

    @Column(nullable = false)
    private LocalDateTime smCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studys_id")
    private Studys studys;
}
