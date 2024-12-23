package com.example.skillhub.controllers;

import com.example.skillhub.domain.*;
import com.example.skillhub.domain.dto.ProgressDTO;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.services.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/progresses")
public class ProgressController {

    private final ProgressService progressService;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final SectionService sectionService;
    private final AuthorService authorService;

    @Autowired
    public ProgressController(ProgressService progressService,
                              CourseService courseService,
                              LessonService lessonService,
                              SectionService sectionService,
                              AuthorService authorService) {
        this.progressService = progressService;
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.sectionService = sectionService;
        this.authorService = authorService;
    }

    @Operation(summary = "Create a new progress record", description = "Creates a new progress record for a user on a specific course, lesson, and section.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Progress created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Progress.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to track progress on this course",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course, Lesson, Section, or Author not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Progress> createProgress(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Progress details to create", required = true) ProgressDTO progressDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Validate and fetch related entities
        Course course = courseService.getCourseById(progressDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + progressDTO.getCourseId()));

        Lesson lesson = lessonService.getLessonById(progressDTO.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + progressDTO.getLessonId()));

        Section section = sectionService.getSectionById(progressDTO.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + progressDTO.getSectionId()));

        // Check if the user is authorized to track progress on the course
        boolean isAuthorized = courseService.isUserAuthorized(course.getId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Progress progress = Progress.builder()
                .user(author)
                .course(course)
                .lesson(lesson)
                .section(section)
                .status(progressDTO.getStatus())
                .createdAt(new Date())
                .build();

        Progress createdProgress = progressService.createProgress(progress);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProgress);
    }

    @Operation(summary = "Retrieve a progress record by ID", description = "Fetches a single progress record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Progress.class))),
            @ApiResponse(responseCode = "404", description = "Progress not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Progress> getProgressById(
            @Parameter(description = "ID of the progress to retrieve", required = true) @PathVariable Long id) {
        Progress progress = progressService.getProgressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found with id " + id));
        return ResponseEntity.ok(progress);
    }

    @Operation(summary = "Retrieve all progress records", description = "Fetches a list of all progress records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress records retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Progress.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Progress>> getAllProgresses() {
        List<Progress> progresses = progressService.getAllProgresses();
        return ResponseEntity.ok(progresses);
    }

    @Operation(summary = "Update a progress record partially", description = "Updates specific fields of an existing progress record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progress updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Progress.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to update this progress",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Progress not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Progress> updateProgress(
            @Parameter(description = "ID of the progress to update", required = true) @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Progress details to update", required = true) ProgressDTO progressDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Progress existingProgress = progressService.getProgressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found with id " + id));

        // Check if the user is the owner of the progress or authorized to update
        if (!existingProgress.getUser().getId().equals(author.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Optionally, update other fields like lesson, section, etc.
        Progress progressDetails = Progress.builder()
                .status(progressDTO.getStatus())
                .createdAt(existingProgress.getCreatedAt()) // Preserve creation date
                .user(existingProgress.getUser())
                .course(existingProgress.getCourse())
                .lesson(existingProgress.getLesson())
                .section(existingProgress.getSection())
                .build();

        Progress updatedProgress = progressService.partialUpdate(id, progressDetails);
        return ResponseEntity.ok(updatedProgress);
    }

    @Operation(summary = "Delete a progress record", description = "Deletes an existing progress record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Progress deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to delete this progress",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Progress not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(
            @Parameter(description = "ID of the progress to delete", required = true) @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Progress progress = progressService.getProgressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found with id " + id));

        // Check if the user is the owner of the progress
        if (!progress.getUser().getId().equals(author.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        progressService.deleteProgress(id);
        return ResponseEntity.noContent().build();
    }

}
