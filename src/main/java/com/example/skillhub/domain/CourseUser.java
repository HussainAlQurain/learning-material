package com.example.skillhub.domain;

import com.example.skillhub.enums.Status;
import com.example.skillhub.enums.UserRoleInCourse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Author user;

    @Enumerated(EnumType.STRING)
    private UserRoleInCourse role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

}
