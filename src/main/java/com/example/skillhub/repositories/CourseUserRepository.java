package com.example.skillhub.repositories;

import com.example.skillhub.domain.CourseUser;
import com.example.skillhub.enums.UserRoleInCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {
    boolean existsByCourseIdAndUserIdAndRole(Long courseId, Long userId, UserRoleInCourse role);
    List<CourseUser> findByCourseId(Long courseId);

}
