package com.example.skillhub.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request body for registering a new author")
public class RegisterRequest {

    @NotBlank(message = "Name is mandatory")
    @Schema(description = "Name of the author", example = "John Doe", required = true)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Schema(description = "Email address of the author", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Schema(description = "Phone number of the author", example = "1234567890", required = true)
    private String phone;

    @NotBlank(message = "Password is mandatory")
    @Schema(description = "Password for the author's account", example = "password123", required = true)
    private String password;

    @NotBlank(message = "Role is mandatory")
    @Schema(description = "Role assigned to the author", example = "ROLE_USER", required = true)
    private String role; // e.g., "ROLE_USER", "ROLE_ADMIN"
}
