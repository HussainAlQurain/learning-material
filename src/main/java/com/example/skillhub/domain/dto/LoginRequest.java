package com.example.skillhub.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request body for user login")
public class LoginRequest {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Schema(description = "Email address of the author", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Schema(description = "Password of the author", example = "password123", required = true)
    private String password;
}
