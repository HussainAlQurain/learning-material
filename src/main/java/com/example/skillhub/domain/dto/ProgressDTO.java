package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.ProgressStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgressDTO {
    @NotNull(message = "Course ID is mandatory")
    private Long courseId;

    @NotNull(message = "Lesson ID is mandatory")
    private Long lessonId;

    @NotNull(message = "Section ID is mandatory")
    private Long sectionId;

    @NotNull(message = "Progress status is mandatory")
    private ProgressStatus status; // e.g., NOT_STARTED, IN_PROGRESS, COMPLETED
}
