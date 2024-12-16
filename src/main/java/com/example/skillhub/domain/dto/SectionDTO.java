package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SectionDTO {
    @NotNull(message = "Lesson ID is mandatory")
    private Long lessonId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private ContentType contentType;
    private String contentUrl;
    private String contentText;
}
