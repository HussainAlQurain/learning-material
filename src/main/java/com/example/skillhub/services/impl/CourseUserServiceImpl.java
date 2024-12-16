package com.example.skillhub.services.impl;

import com.example.skillhub.domain.CourseUser;
import com.example.skillhub.enums.Status;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.repositories.CourseUserRepository;
import com.example.skillhub.services.CourseUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseUserServiceImpl implements CourseUserService {
    private final CourseUserRepository courseUserRepository;

    @Autowired
    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public CourseUser createCourseUser(CourseUser courseUser) {
        courseUser.setCreatedAt(new Date());
        courseUser.setStatus(Status.ACTIVE);
        return courseUserRepository.save(courseUser);
    }

    @Override
    public CourseUser partialUpdate(Long id, CourseUser courseUserDetails) {
        return courseUserRepository.findById(id).map(existingCourseUser -> {
            Optional.ofNullable(courseUserDetails.getCourse()).ifPresent(existingCourseUser::setCourse);
            Optional.ofNullable(courseUserDetails.getUser()).ifPresent(existingCourseUser::setUser);
            Optional.ofNullable(courseUserDetails.getRole()).ifPresent(existingCourseUser::setRole);
            Optional.ofNullable(courseUserDetails.getStatus()).ifPresent(existingCourseUser::setStatus);
            // Optionally update createdAt if needed
            return courseUserRepository.save(existingCourseUser);
        }).orElseThrow(() -> new ResourceNotFoundException("CourseUser not found with id " + id));
    }

    @Override
    public Optional<CourseUser> getCourseUserById(Long id) {
        return courseUserRepository.findById(id);
    }

    @Override
    public List<CourseUser> getAllCourseUsers() {
        return courseUserRepository.findAll();
    }

    @Override
    public void deleteCourseUser(Long id) {
        CourseUser courseUser = courseUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CourseUser not found with id " + id));
        courseUserRepository.delete(courseUser);
    }
}
