package com.example.skillhub.domain;

import com.example.skillhub.enums.Rating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;

    private String role; //creator, learner..
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    private String status; //active inactive

    @Enumerated(EnumType.STRING)
    private Rating rating;

    // Consider using OneToMany
}
