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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Section> createSection(@Valid @RequestBody SectionDTO sectionDTO,
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

    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long id) {
        Section section = sectionService.getSectionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + id));
        return ResponseEntity.ok(section);
    }

    @GetMapping
    public ResponseEntity<List<Section>> getAllSections() {
        List<Section> sections = sectionService.getAllSections();
        return ResponseEntity.ok(sections);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable Long id,
                                                 @Valid @RequestBody SectionDTO sectionDTO,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id,
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
