package com.example.skillhub.services.impl;

import com.example.skillhub.domain.Lesson;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.repositories.LessonRepository;
import com.example.skillhub.services.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson createLesson(Lesson lesson) {
        lesson.setCreatedAt(new Date());
        lesson.setStatus("active"); // default status
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson partialUpdate(Long id, Lesson lessonDetails) {
        return lessonRepository.findById(id).map(existingLesson -> {
            Optional.ofNullable(lessonDetails.getCourse()).ifPresent(existingLesson::setCourse);
            Optional.ofNullable(lessonDetails.getTitle()).ifPresent(existingLesson::setTitle);
            Optional.ofNullable(lessonDetails.getDescription()).ifPresent(existingLesson::setDescription);
            Optional.ofNullable(lessonDetails.getDuration()).ifPresent(existingLesson::setDuration);
            Optional.ofNullable(lessonDetails.getLevel()).ifPresent(existingLesson::setLevel);
            Optional.ofNullable(lessonDetails.getStatus()).ifPresent(existingLesson::setStatus);
            Optional.ofNullable(lessonDetails.getRating()).ifPresent(existingLesson::setRating);
            // Update createdAt if needed
            return lessonRepository.save(existingLesson);
        }).orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));
    }

    @Override
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id " + id));
        lessonRepository.delete(lesson);
    }
}
