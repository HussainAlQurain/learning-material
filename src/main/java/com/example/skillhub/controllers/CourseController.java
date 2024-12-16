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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseDTO courseDTO,
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

        // Automatically add the creator as a CourseUser with role OWNER
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

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id,
                                               @Valid @RequestBody CourseDTO courseDTO,
                                               Authentication authentication) {
        String email = authentication.getName();
        Author author = authorService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        // Check if the user is the owner or a collaborator
        boolean isAuthorized = courseService.isUserAuthorized(id, author);
        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id,
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
