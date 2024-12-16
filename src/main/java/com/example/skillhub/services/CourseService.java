package com.example.skillhub.services;

import com.example.skillhub.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);
    Course partialUpdate(Long id, Course course);
    Optional<Course> getCourseById(Long id);
    List<Course> getAllCourses();
    void deleteCourse(Long id);
}
