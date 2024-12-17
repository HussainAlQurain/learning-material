package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for creating and updating courses")
public class CourseDTO {

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

    @Schema(description = "Rating of the course", example = "FIVE", required = false)
    private Rating rating;

    // Optional fields

    @Schema(description = "Email of the user who created the course", example = "john.doe@example.com", required = false)
    private String createdBy;

    @Schema(description = "Email of the user who last modified the course", example = "jane.doe@example.com", required = false)
    private String lastModifiedBy;
}
