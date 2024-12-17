package com.example.skillhub.controllers;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.Course;
import com.example.skillhub.domain.Lesson;
import com.example.skillhub.domain.dto.LessonDTO;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.services.AuthorService;
import com.example.skillhub.services.CourseService;
import com.example.skillhub.services.LessonService;
import com.example.skillhub.services.SectionService;
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

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final CourseService courseService;
    private final AuthorService authorService;

    @Autowired
    public LessonController(LessonService lessonService,
                            CourseService courseService,
                            AuthorService authorService) {
        this.lessonService = lessonService;
        this.courseService = courseService;
        this.authorService = authorService;
    }

    @Operation(summary = "Create a new lesson", description = "Creates a new lesson associated with a specific course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lesson created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to add lessons to this course",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Lesson> createLesson(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Lesson details to create", required = true) LessonDTO lessonDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is authorized to add lessons to the course
        boolean isAuthorized = courseService.isUserAuthorized(lessonDTO.getCourseId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Lesson lesson = Lesson.builder()
                .title(lessonDTO.getTitle())
                .description(lessonDTO.getDescription())
                .duration(lessonDTO.getDuration())
                .level(lessonDTO.getLevel())
                .status("active")
                .rating(lessonDTO.getRating())
                .createdAt(new java.util.Date())
                .course(Course.builder().id(lessonDTO.getCourseId()).build())
                .build();

        Lesson createdLesson = lessonService.createLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @Operation(summary = "Retrieve a lesson by ID", description = "Fetches a single lesson by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(
            @Parameter(description = "ID of the lesson to retrieve", required = true) @PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));
        return ResponseEntity.ok(lesson);
    }

    @Operation(summary = "Retrieve all lessons", description = "Fetches a list of all available lessons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Lesson.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @Operation(summary = "Update a lesson partially", description = "Updates specific fields of an existing lesson.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to update this lesson",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson or Course not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(
            @Parameter(description = "ID of the lesson to update", required = true) @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Lesson details to update", required = true) LessonDTO lessonDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Lesson existingLesson = lessonService.getLessonById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));

        // Check if the user is authorized to update the lesson's course
        boolean isAuthorized = courseService.isUserAuthorized(existingLesson.getCourse().getId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Lesson lessonDetails = Lesson.builder()
                .title(lessonDTO.getTitle())
                .description(lessonDTO.getDescription())
                .duration(lessonDTO.getDuration())
                .level(lessonDTO.getLevel())
                .status(lessonDTO.getStatus())
                .rating(lessonDTO.getRating())
                .createdAt(existingLesson.getCreatedAt()) // Preserve creation date
                .course(existingLesson.getCourse()) // Preserve associated course
                .build();

        Lesson updatedLesson = lessonService.partialUpdate(id, lessonDetails);
        return ResponseEntity.ok(updatedLesson);
    }

    @Operation(summary = "Delete a lesson", description = "Deletes an existing lesson by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lesson deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to delete this lesson",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(
            @Parameter(description = "ID of the lesson to delete", required = true) @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Lesson lesson = lessonService.getLessonById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));

        // Check if the user is authorized to delete the lesson's course
        boolean isAuthorized = courseService.isUserAuthorized(lesson.getCourse().getId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

}
