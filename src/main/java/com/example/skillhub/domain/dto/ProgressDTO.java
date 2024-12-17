package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.ProgressStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for tracking user progress in a course")
public class ProgressDTO {

    @NotNull(message = "Course ID is mandatory")
    @Schema(description = "Unique identifier of the course", example = "1", required = true)
    private Long courseId;

    @NotNull(message = "Lesson ID is mandatory")
    @Schema(description = "Unique identifier of the lesson", example = "10", required = true)
    private Long lessonId;

    @NotNull(message = "Section ID is mandatory")
    @Schema(description = "Unique identifier of the section", example = "100", required = true)
    private Long sectionId;

    @NotNull(message = "Progress status is mandatory")
    @Schema(description = "Current progress status", example = "IN_PROGRESS", required = true)
    private ProgressStatus status; // e.g., NOT_STARTED, IN_PROGRESS, COMPLETED
}
