package com.example.skillhub.domain;

import com.example.skillhub.enums.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Schema(description = "Entity representing an author in the SkillHub platform")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the author", example = "1", required = true)
    private Long id;

    @Schema(description = "Name of the author", example = "John Doe", required = true)
    private String name;

    @Column(unique = true)
    @Schema(description = "Email address of the author", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Phone number of the author", example = "1234567890", required = true)
    private String phone;

    @Schema(description = "Password for the author's account", example = "password123", required = true)
    private String password;

    @Schema(description = "Role assigned to the author, e.g., 'ROLE_USER', 'ROLE_ADMIN'", example = "ROLE_USER", required = true)
    private String role; //creator, learner..

    @Temporal(TemporalType.DATE)
    @Schema(description = "Date when the author's account was created", example = "2024-12-17", required = true)
    private Date createdAt;

    @Schema(description = "Current status of the author's account, e.g., 'active', 'inactive'", example = "active", required = true)
    private String status; //active inactive

    @Enumerated(EnumType.STRING)
    @Schema(description = "Rating of the author", example = "FIVE", required = false)
    private Rating rating;

    // Relationships (if any) can be annotated similarly
}
