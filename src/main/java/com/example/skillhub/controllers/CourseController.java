package com.example.skillhub.controllers;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.Course;
import com.example.skillhub.domain.CourseUser;
import com.example.skillhub.domain.dto.CourseDTO;
import com.example.skillhub.enums.Status;
import com.example.skillhub.enums.UserRoleInCourse;
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
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseUserService courseUserService;
    private final AuthorService authorService;

    @Autowired
    public CourseController(CourseService courseService,
                            CourseUserService courseUserService,
                            AuthorService authorService) {
        this.courseService = courseService;
        this.courseUserService = courseUserService;
        this.authorService = authorService;
    }

    @Operation(summary = "Create a new course", description = "Creates a new course and assigns the creator as the course owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Course> createCourse(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Course details to create", required = true) CourseDTO courseDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Course course = Course.builder()
                .title(courseDTO.getTitle())
                .description(courseDTO.getDescription())
                .image(courseDTO.getImage())
                .video(courseDTO.getVideo())
                .category(courseDTO.getCategory())
                .duration(courseDTO.getDuration())
                .price(courseDTO.getPrice())
                .level(courseDTO.getLevel())
                .status("active")
                .rating(courseDTO.getRating())
                .createdDate(new java.util.Date())
                .lastModifiedDate(new java.util.Date())
                .user(author)
                .createdBy(author.getEmail())
                .lastModifiedBy(author.getEmail())
                .build();

        Course createdCourse = courseService.createCourse(course);

        // Automatically add the creator as a CourseUser with role AUTHOR
        CourseUser courseUser = CourseUser.builder()
                .course(createdCourse)
                .user(author)
                .role(UserRoleInCourse.AUTHOR)
                .status(Status.ACTIVE)
                .createdAt(new java.util.Date())
                .build();

        courseUserService.createCourseUser(courseUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @Operation(summary = "Retrieve a course by ID", description = "Fetches a single course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
            @Parameter(description = "ID of the course to retrieve", required = true) @PathVariable Long id) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        return ResponseEntity.ok(course);
    }

    @Operation(summary = "Retrieve all courses", description = "Fetches a list of all available courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Course.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Update a course partially", description = "Updates specific fields of an existing course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to update this course",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @Parameter(description = "ID of the course to update", required = true) @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Course details to update", required = true) CourseDTO courseDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is the owner or a collaborator
        boolean isAuthorized = courseService.isUserAuthorized(id, author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Course courseDetails = Course.builder()
                .title(courseDTO.getTitle())
                .description(courseDTO.getDescription())
                .image(courseDTO.getImage())
                .video(courseDTO.getVideo())
                .category(courseDTO.getCategory())
                .duration(courseDTO.getDuration())
                .price(courseDTO.getPrice())
                .level(courseDTO.getLevel())
                .status(courseDTO.getStatus())
                .rating(courseDTO.getRating())
                .lastModifiedDate(new java.util.Date())
                .lastModifiedBy(author.getEmail())
                .user(author) // Update the user if needed
                .createdBy(courseDTO.getCreatedBy()) // Typically, createdBy shouldn't change
                .build();

        Course updatedCourse = courseService.partialUpdate(id, courseDetails);
        return ResponseEntity.ok(updatedCourse);
    }

    @Operation(summary = "Delete a course", description = "Deletes an existing course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to delete this course",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "ID of the course to delete", required = true) @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is the owner or a collaborator
        boolean isAuthorized = courseService.isUserAuthorized(id, author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
