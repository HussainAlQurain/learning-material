package com.example.skillhub.repositories;

import com.example.skillhub.domain.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {
}
