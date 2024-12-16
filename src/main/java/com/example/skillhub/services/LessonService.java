package com.example.skillhub.services;

import com.example.skillhub.domain.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Lesson createLesson(Lesson lesson);
    Lesson partialUpdate(Long id, Lesson lesson);
    Optional<Lesson> getLessonById(Long id);
    List<Lesson> getAllLessons();
    void deleteLesson(Long id);
}
