package com.example.skillhub.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing the roles a user can have within a course")
public enum UserRoleInCourse {

    @Schema(description = "Student enrolled in the course")
    STUDENT,

    @Schema(description = "Instructor responsible for teaching the course")
    INSTRUCTOR,

    @Schema(description = "Teaching Assistant supporting the instructor")
    TEACHING_ASSISTANT,

    @Schema(description = "Author who created the course content")
    AUTHOR,

    @Schema(description = "Collaborator assisting in course management or content creation")
    COLLABORATOR,

    @Schema(description = "Administrator with full permissions across the platform")
    ADMIN;
}
