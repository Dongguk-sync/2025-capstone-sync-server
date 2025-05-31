package com.baekji.study.domain;

import com.baekji.common.enums.COMPLECTED;
import com.baekji.subject.domain.AnswerFile;
import com.baekji.subject.domain.Subject;
import com.baekji.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyScheduleId;

    @Column(nullable = false)
    private LocalDateTime studyScheduleDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private COMPLECTED studyScheduleCompleted; // COMP, UNCOMP

    @Column(nullable = false)
    private LocalDateTime studyScheduleCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private AnswerFile answerFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;
}
