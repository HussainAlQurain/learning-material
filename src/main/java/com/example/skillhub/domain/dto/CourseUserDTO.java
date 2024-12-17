package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.UserRoleInCourse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for adding or updating course collaborators")
public class CourseUserDTO {

    @NotNull(message = "Course ID is mandatory")
    @Schema(description = "Unique identifier of the course", example = "1", required = true)
    private Long courseId;

    @Email(message = "Collaborator email should be valid")
    @NotBlank(message = "Collaborator email is mandatory")
    @Schema(description = "Email address of the collaborator to be added", example = "collaborator@example.com", required = true)
    private String collaboratorEmail;

    @NotNull(message = "Role is mandatory")
    @Schema(description = "Role assigned to the collaborator in the course", example = "COLLABORATOR", required = true)
    private UserRoleInCourse role; // e.g., COLLABORATOR, REVIEWER
}
