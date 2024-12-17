package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for creating and updating lessons")
public class LessonDTO {

    @NotNull(message = "Course ID is mandatory")
    @Schema(description = "Unique identifier of the course to which the lesson belongs", example = "1", required = true)
    private Long courseId;

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "Title of the lesson", example = "Introduction to Java", required = true)
    private String title;

    @Schema(description = "Detailed description of the lesson", example = "This lesson covers the basics of Java programming.")
    private String description;

    @Schema(description = "Duration of the lesson", example = "2 hours")
    private String duration;

    @Schema(description = "Difficulty level of the lesson", example = "Beginner")
    private String level;

    @Schema(description = "Current status of the lesson", example = "active")
    private String status;

    @Schema(description = "Rating of the lesson", example = "FIVE", required = false)
    private Rating rating;
}
