package com.example.skillhub.domain;

import com.example.skillhub.enums.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Entity representing a course in the SkillHub platform")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the course", example = "1", required = true)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "Title of the course", example = "Introduction to Java", required = true)
    private String title;

    @Schema(description = "Detailed description of the course", example = "Learn the fundamentals of Java programming.")
    private String description;

    @Schema(description = "URL of the course image", example = "http://example.com/images/java-course.png")
    private String image;

    @Schema(description = "URL of the course introductory video", example = "http://example.com/videos/java-intro.mp4")
    private String video;

    @Schema(description = "Category under which the course falls", example = "Programming")
    private String category;

    @Schema(description = "Duration of the course", example = "10 hours")
    private String duration;

    @Schema(description = "Price of the course", example = "49.99")
    private String price;

    @Schema(description = "Difficulty level of the course", example = "Beginner")
    private String level;

    @Schema(description = "Current status of the course", example = "active")
    private String status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Rating of the course", example = "FIVE", required = false)
    private Rating rating;

    @Temporal(TemporalType.DATE)
    @Schema(description = "Date when the course was created", example = "2024-12-17", required = true)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Timestamp of the last modification made to the course", example = "2024-12-17T10:20:30Z", required = true)
    private Date lastModifiedDate;

    @ManyToOne
    @Schema(description = "Author who created the course")
    private Author user;

    @Schema(description = "Email of the user who created the course", example = "john.doe@example.com")
    private String createdBy;

    @Schema(description = "Email of the user who last modified the course", example = "jane.doe@example.com")
    private String lastModifiedBy;

    // Relationships (e.g., OneToMany with CourseUser) can be annotated similarly
}
