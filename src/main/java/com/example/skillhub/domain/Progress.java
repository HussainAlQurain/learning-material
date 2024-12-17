package com.example.skillhub.domain;

import com.example.skillhub.enums.ProgressStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entity representing the progress of a user within a course")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the progress record", example = "100", required = true)
    private Long id;

    @ManyToOne
    @Schema(description = "User associated with this progress")
    private Author user;

    @ManyToOne
    @Schema(description = "Course associated with this progress")
    private Course course;

    @ManyToOne
    @Schema(description = "Lesson associated with this progress")
    private Lesson lesson;

    @ManyToOne
    @Schema(description = "Section associated with this progress")
    private Section section;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Current status of the progress", example = "IN_PROGRESS", required = true)
    private ProgressStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Timestamp when the progress was recorded", example = "2024-12-17T10:25:30Z", required = true)
    private Date createdAt;
}
