package com.example.skillhub.domain.dto;

import com.example.skillhub.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AuthorProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Rating rating;
    private Date createdAt;
}
