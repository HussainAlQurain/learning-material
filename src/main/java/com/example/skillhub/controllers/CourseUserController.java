package com.example.skillhub.controllers;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.Course;
import com.example.skillhub.domain.CourseUser;
import com.example.skillhub.domain.dto.CourseUserDTO;
import com.example.skillhub.enums.Status;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.services.AuthorService;
import com.example.skillhub.services.CourseService;
import com.example.skillhub.services.CourseUserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-users")
public class CourseUserController {
    private final CourseUserService courseUserService;
    private final CourseService courseService;
    private final AuthorService authorService;

    @Autowired
    public CourseUserController(CourseUserService courseUserService,
                                CourseService courseService,
                                AuthorService authorService) {
        this.courseUserService = courseUserService;
        this.courseService = courseService;
        this.authorService = authorService;
    }

    @Operation(summary = "Add a collaborator to a course", description = "Adds a collaborator with a specific role to the specified course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Collaborator added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseUser.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to add collaborators",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course or collaborator not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CourseUser> addCollaborator(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CourseUser details to add a collaborator", required = true) CourseUserDTO courseUserDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is the owner of the course
        Course course = courseService.getCourseById(courseUserDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + courseUserDTO.getCourseId()));

        if (!course.getUser().getId().equals(author.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Author collaborator = authorService.findByEmail(courseUserDTO.getCollaboratorEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator not found with email " + courseUserDTO.getCollaboratorEmail()));

        CourseUser courseUser = CourseUser.builder()
                .course(course)
                .user(collaborator)
                .role(courseUserDTO.getRole())
                .status(Status.ACTIVE)
                .createdAt(new java.util.Date())
                .build();

        CourseUser createdCourseUser = courseUserService.createCourseUser(courseUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourseUser);
    }

    @Operation(summary = "Remove a collaborator from a course", description = "Removes a collaborator from the specified course by their CourseUser ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Collaborator removed successfully",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to remove collaborators",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "CourseUser not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCollaborator(
            @Parameter(description = "ID of the CourseUser to remove", required = true) @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        CourseUser courseUser = courseUserService.getCourseUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CourseUser not found with id " + id));

        // Only the course owner can remove collaborators
        if (!courseUser.getCourse().getUser().getId().equals(author.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        courseUserService.deleteCourseUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all collaborators for a course", description = "Retrieves a list of all collaborators associated with the specified course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collaborators retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseUser.class)))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to view collaborators",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)
    })
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseUser>> getCollaborators(
            @Parameter(description = "ID of the course to retrieve collaborators for", required = true) @PathVariable Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is authorized to view collaborators
        boolean isAuthorized = courseService.isUserAuthorized(courseId, author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CourseUser> collaborators = courseUserService.findByCourseId(courseId);
        return ResponseEntity.ok(collaborators);
    }

}
