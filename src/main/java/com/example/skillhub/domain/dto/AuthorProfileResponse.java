package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Schema(description = "Response containing the author's profile details")
public class AuthorProfileResponse {

    @Schema(description = "Unique identifier of the author", example = "1", required = true)
    private Long id;

    @Schema(description = "Name of the author", example = "John Doe", required = true)
    private String name;

    @Schema(description = "Email address of the author", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Phone number of the author", example = "1234567890", required = true)
    private String phone;

    @Schema(description = "Role of the author", example = "ROLE_USER", required = true)
    private String role;

    @Schema(description = "Status of the author", example = "active", required = true)
    private String status;

    @Schema(description = "Rating of the author", example = "ONE", required = false)
    private Rating rating;

    @Schema(description = "Creation timestamp of the author's profile", example = "2024-12-17T08:20:13.239+00:00", required = true)
    private Date createdAt;
}
