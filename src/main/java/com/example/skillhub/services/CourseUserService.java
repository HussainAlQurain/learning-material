package com.example.skillhub.services;

import com.example.skillhub.domain.CourseUser;

import java.util.List;
import java.util.Optional;

public interface CourseUserService {
    CourseUser createCourseUser(CourseUser courseUser);
    CourseUser partialUpdate(Long id, CourseUser courseUser);
    Optional<CourseUser> getCourseUserById(Long id);
    List<CourseUser> getAllCourseUsers();
    void deleteCourseUser(Long id);
}
