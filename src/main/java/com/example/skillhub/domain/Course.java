package com.example.skillhub.domain;

import com.example.skillhub.enums.Rating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    private String description;
    private String image;
    private String video;
    private String category;
    private String duration;
    private String price;
    private String level;
    private String status;
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @ManyToOne
    private Author user;
    private String createdBy;
    private String lastModifiedBy;

    // consider using onetomany with courseuser.
}

