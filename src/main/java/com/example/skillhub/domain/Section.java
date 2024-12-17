package com.example.skillhub.domain;

import com.example.skillhub.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Schema(description = "Entity representing a section within a lesson")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the section", example = "100", required = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonBackReference
    @Schema(description = "Lesson to which this section belongs")
    private Lesson lesson;

    @Schema(description = "Title of the section", example = "Introduction to Variables", required = true)
    private String title;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of content in the section", example = "TEXT", required = true)
    private ContentType contentType;

    @Schema(description = "URL to the content resource", example = "http://example.com/content/variables")
    private String contentUrl;

    @Schema(description = "Textual content of the section", example = "Variables are used to store data...")
    private String contentText;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Timestamp when the section was created", example = "2024-12-17T10:30:00Z", required = true)
    private Date createdAt;
}
