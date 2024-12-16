package com.example.skillhub.controllers;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.Course;
import com.example.skillhub.domain.Lesson;
import com.example.skillhub.domain.dto.LessonDTO;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.services.AuthorService;
import com.example.skillhub.services.CourseService;
import com.example.skillhub.services.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody LessonDTO lessonDTO,
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

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.getLessonById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));
        return ResponseEntity.ok(lesson);
    }

    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id,
                                               @Valid @RequestBody LessonDTO lessonDTO,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id,
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
