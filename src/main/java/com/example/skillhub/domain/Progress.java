package com.example.skillhub.domain;


import com.example.skillhub.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Author user;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Lesson lesson;

    @ManyToOne
    private Section section;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

}
