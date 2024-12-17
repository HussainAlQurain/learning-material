package com.example.skillhub.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response containing messages related to authentication operations")
public class AuthResponse {

    @Schema(description = "Message describing the result of the authentication operation", example = "User registered successfully", required = true)
    private String message;
}
