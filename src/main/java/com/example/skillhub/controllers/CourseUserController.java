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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<CourseUser> addCollaborator(@Valid @RequestBody CourseUserDTO courseUserDTO,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCollaborator(@PathVariable Long id,
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

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseUser>> getCollaborators(@PathVariable Long courseId,
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
