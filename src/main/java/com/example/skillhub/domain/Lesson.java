package com.example.skillhub.domain;

import com.example.skillhub.enums.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Schema(description = "Entity representing a lesson within a course")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the lesson", example = "10", required = true)
    private Long id;

    @ManyToOne
    @Schema(description = "Course to which this lesson belongs")
    private Course course;

    @Schema(description = "Title of the lesson", example = "Introduction to Variables", required = true)
    private String title;

    @Schema(description = "Detailed description of the lesson", example = "This lesson covers the basics of variables in Java.")
    private String description;

    @Schema(description = "Duration of the lesson", example = "2 hours")
    private String duration;

    @Schema(description = "Difficulty level of the lesson", example = "Beginner")
    private String level;

    @Schema(description = "Current status of the lesson", example = "active")
    private String status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Rating of the lesson", example = "FOUR", required = false)
    private Rating rating;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Timestamp when the lesson was created", example = "2024-12-17T10:20:30Z", required = true)
    private Date createdAt;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "List of sections within this lesson")
    private List<Section> sections;
}
