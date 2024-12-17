package com.example.skillhub.controllers;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.Lesson;
import com.example.skillhub.domain.Section;
import com.example.skillhub.domain.dto.SectionDTO;
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
@RequestMapping("/api/sections")
public class SectionController {
    private final SectionService sectionService;
    private final LessonService lessonService;
    private final CourseService courseService;
    private final AuthorService authorService;


    @Autowired
    public SectionController(SectionService sectionService,
                             LessonService lessonService,
                             CourseService courseService,
                             AuthorService authorService) {
        this.sectionService = sectionService;
        this.lessonService = lessonService;
        this.courseService = courseService;
        this.authorService = authorService;
    }

    @Operation(summary = "Create a new section", description = "Creates a new section associated with a specific lesson.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Section created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Section.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to add sections to this lesson",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Section> createSection(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Section details to create", required = true) SectionDTO sectionDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is authorized to add sections to the lesson's course
        Lesson lesson = lessonService.getLessonById(sectionDTO.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + sectionDTO.getLessonId()));

        boolean isAuthorized = courseService.isUserAuthorized(lesson.getCourse().getId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Section section = Section.builder()
                .title(sectionDTO.getTitle())
                .contentType(sectionDTO.getContentType())
                .contentUrl(sectionDTO.getContentUrl())
                .contentText(sectionDTO.getContentText())
                .createdAt(new java.util.Date())
                .lesson(lesson)
                .build();

        Section createdSection = sectionService.createSection(section);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
    }

    @Operation(summary = "Retrieve a section by ID", description = "Fetches a single section by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Section retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Section.class))),
            @ApiResponse(responseCode = "404", description = "Section not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(
            @Parameter(description = "ID of the section to retrieve", required = true) @PathVariable Long id) {
        Section section = sectionService.getSectionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + id));
        return ResponseEntity.ok(section);
    }

    @Operation(summary = "Retrieve all sections", description = "Fetches a list of all available sections.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sections retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Section.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Section>> getAllSections() {
        List<Section> sections = sectionService.getAllSections();
        return ResponseEntity.ok(sections);
    }

    @Operation(summary = "Update a section partially", description = "Updates specific fields of an existing section.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Section updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Section.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to update this section",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Section or Course not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(
            @Parameter(description = "ID of the section to update", required = true) @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Section details to update", required = true) SectionDTO sectionDTO,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Section existingSection = sectionService.getSectionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + id));

        // Check if the user is authorized to update the section's lesson's course
        boolean isAuthorized = courseService.isUserAuthorized(existingSection.getLesson().getCourse().getId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Section sectionDetails = Section.builder()
                .title(sectionDTO.getTitle())
                .contentType(sectionDTO.getContentType())
                .contentUrl(sectionDTO.getContentUrl())
                .contentText(sectionDTO.getContentText())
                .createdAt(existingSection.getCreatedAt()) // Preserve creation date
                .lesson(existingSection.getLesson()) // Preserve associated lesson
                .build();

        Section updatedSection = sectionService.partialUpdate(id, sectionDetails);
        return ResponseEntity.ok(updatedSection);
    }

    @Operation(summary = "Delete a section", description = "Deletes an existing section by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Section deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to delete this section",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Section not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(
            @Parameter(description = "ID of the section to delete", required = true) @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Section section = sectionService.getSectionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + id));

        // Check if the user is authorized to delete the section's lesson's course
        boolean isAuthorized = courseService.isUserAuthorized(section.getLesson().getCourse().getId(), author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }

}
