package com.example.skillhub.domain;

import com.example.skillhub.enums.Status;
import com.example.skillhub.enums.UserRoleInCourse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Schema(description = "Entity representing the association between a course and a user, specifying the user's role and status within the course")
public class CourseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the CourseUser association", example = "1", required = true)
    private Long id;

    @ManyToOne
    @Schema(description = "Course associated with the user")
    private Course course;

    @ManyToOne
    @Schema(description = "User associated with the course")
    private Author user;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Role of the user within the course", example = "COLLABORATOR", required = true)
    private UserRoleInCourse role;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Current status of the user's association with the course", example = "ACTIVE", required = true)
    private Status status;

    @Temporal(TemporalType.DATE)
    @Schema(description = "Date when the association was created", example = "2024-12-17", required = true)
    private Date createdAt;
}
