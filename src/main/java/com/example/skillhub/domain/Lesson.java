package com.example.skillhub.domain;

import com.example.skillhub.enums.Rating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    private String title;
    private String description;
    private String duration;
    private String level;
    private String status;
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
