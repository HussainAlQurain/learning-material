package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.UserRoleInCourse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseUserDTO {
    @NotNull(message = "Course ID is mandatory")
    private Long courseId;

    @Email(message = "Collaborator email should be valid")
    @NotBlank(message = "Collaborator email is mandatory")
    private String collaboratorEmail;

    @NotNull(message = "Role is mandatory")
    private UserRoleInCourse role; // e.g., COLLABORATOR, REVIEWER
}
