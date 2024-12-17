package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for creating and updating sections within a lesson")
public class SectionDTO {

    @NotNull(message = "Lesson ID is mandatory")
    @Schema(description = "Unique identifier of the lesson to which the section belongs", example = "10", required = true)
    private Long lessonId;

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "Title of the section", example = "Introduction to Variables", required = true)
    private String title;

    @Schema(description = "Type of content in the section", example = "TEXT")
    private ContentType contentType;

    @Schema(description = "URL to the content resource", example = "http://example.com/content/variables")
    private String contentUrl;

    @Schema(description = "Textual content of the section", example = "Variables are used to store data...")
    private String contentText;
}
