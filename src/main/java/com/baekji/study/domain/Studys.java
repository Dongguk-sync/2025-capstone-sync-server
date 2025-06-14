package com.baekji.study.domain;

import com.baekji.subject.domain.AnswerFile;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "studys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Studys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studysId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String studysSttContent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String studysFeedContent;

    @Column(nullable = false)
    private String studysSubjectName;

    @Column(nullable = false)
    private LocalDateTime studysCreatedAt;

    @Column(nullable = false)
    private Long studysRound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_schedule_id")
    private StudySchedule studySchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private AnswerFile answerFile;
}
