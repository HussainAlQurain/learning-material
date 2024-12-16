package com.example.skillhub.services.impl;

import com.example.skillhub.domain.Author;
import com.example.skillhub.domain.Course;
import com.example.skillhub.enums.UserRoleInCourse;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.repositories.CourseRepository;
import com.example.skillhub.repositories.CourseUserRepository;
import com.example.skillhub.services.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseUserRepository courseUserRepository) {
        this.courseRepository = courseRepository;
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public Course createCourse(Course course) {
        course.setCreatedDate(new Date());
        course.setLastModifiedDate(new Date());
        course.setStatus("active"); // default status
        return courseRepository.save(course);
    }

    @Override
    public Course partialUpdate(Long id, Course courseDetails) {
        return courseRepository.findById(id).map(existingCourse -> {
            Optional.ofNullable(courseDetails.getTitle()).ifPresent(existingCourse::setTitle);
            Optional.ofNullable(courseDetails.getDescription()).ifPresent(existingCourse::setDescription);
            Optional.ofNullable(courseDetails.getImage()).ifPresent(existingCourse::setImage);
            Optional.ofNullable(courseDetails.getVideo()).ifPresent(existingCourse::setVideo);
            Optional.ofNullable(courseDetails.getCategory()).ifPresent(existingCourse::setCategory);
            Optional.ofNullable(courseDetails.getDuration()).ifPresent(existingCourse::setDuration);
            Optional.ofNullable(courseDetails.getPrice()).ifPresent(existingCourse::setPrice);
            Optional.ofNullable(courseDetails.getLevel()).ifPresent(existingCourse::setLevel);
            Optional.ofNullable(courseDetails.getStatus()).ifPresent(existingCourse::setStatus);
            Optional.ofNullable(courseDetails.getRating()).ifPresent(existingCourse::setRating);
            Optional.ofNullable(courseDetails.getLastModifiedBy()).ifPresent(existingCourse::setLastModifiedBy);
            existingCourse.setLastModifiedDate(new Date());
            Optional.ofNullable(courseDetails.getUser()).ifPresent(existingCourse::setUser);
            Optional.ofNullable(courseDetails.getCreatedBy()).ifPresent(existingCourse::setCreatedBy);
            return courseRepository.save(existingCourse);
        }).orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        courseRepository.delete(course);
    }

    @Override
    public boolean isUserAuthorized(Long courseId, Author author) {
        // Check if the user is the owner
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            return false;
        }
        Course course = courseOpt.get();
        if (course.getUser().getId().equals(author.getId())) {
            return true;
        }

        // Check if the user is a collaborator
        return courseUserRepository.existsByCourseIdAndUserIdAndRole(
                courseId,
                author.getId(),
                UserRoleInCourse.COLLABORATOR
        );
    }

}
