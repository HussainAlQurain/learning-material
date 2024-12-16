package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonDTO {

    @NotNull(message = "Course ID is mandatory")
    private Long courseId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;
    private String duration;
    private String level;
    private String status;
    private Rating rating;
}
