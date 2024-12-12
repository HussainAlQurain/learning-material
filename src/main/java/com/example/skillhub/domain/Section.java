package com.example.skillhub.domain;


import com.example.skillhub.enums.ContentType;
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
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Lesson lesson;
    private String title;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private String contentUrl;
    private String contentText;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

}
