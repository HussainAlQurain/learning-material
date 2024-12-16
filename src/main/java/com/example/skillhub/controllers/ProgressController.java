package com.example.skillhub.controllers;


import com.example.skillhub.domain.*;
import com.example.skillhub.domain.dto.ProgressDTO;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Progress> createProgress(@Valid @RequestBody ProgressDTO progressDTO,
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

    @GetMapping("/{id}")
    public ResponseEntity<Progress> getProgressById(@PathVariable Long id) {
        Progress progress = progressService.getProgressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found with id " + id));
        return ResponseEntity.ok(progress);
    }

    @GetMapping
    public ResponseEntity<List<Progress>> getAllProgresses() {
        List<Progress> progresses = progressService.getAllProgresses();
        return ResponseEntity.ok(progresses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Progress> updateProgress(@PathVariable Long id,
                                                   @Valid @RequestBody ProgressDTO progressDTO,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id,
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
