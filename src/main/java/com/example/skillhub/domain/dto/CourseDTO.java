package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.Rating;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;
    private String image;
    private String video;
    private String category;
    private String duration;
    private String price;
    private String level;
    private String status;
    private Rating rating;

    // Optional fields
    private String createdBy;
    private String lastModifiedBy;

}
