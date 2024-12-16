package com.example.skillhub.services.impl;

import com.example.skillhub.domain.Progress;
import com.example.skillhub.enums.ProgressStatus;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.repositories.ProgressRepository;
import com.example.skillhub.services.ProgressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public Progress createProgress(Progress progress) {
        progress.setCreatedAt(new Date());
        // Set default status if needed
        if (progress.getStatus() == null) {
            progress.setStatus(ProgressStatus.NOT_STARTED); // Assuming NOT_STARTED is a value
        }
        return progressRepository.save(progress);
    }

    @Override
    public Progress partialUpdate(Long id, Progress progressDetails) {
        return progressRepository.findById(id).map(existingProgress -> {
            Optional.ofNullable(progressDetails.getUser()).ifPresent(existingProgress::setUser);
            Optional.ofNullable(progressDetails.getCourse()).ifPresent(existingProgress::setCourse);
            Optional.ofNullable(progressDetails.getLesson()).ifPresent(existingProgress::setLesson);
            Optional.ofNullable(progressDetails.getSection()).ifPresent(existingProgress::setSection);
            Optional.ofNullable(progressDetails.getStatus()).ifPresent(existingProgress::setStatus);
            // Update createdAt if needed
            return progressRepository.save(existingProgress);
        }).orElseThrow(() -> new ResourceNotFoundException("Progress not found with id " + id));
    }

    @Override
    public Optional<Progress> getProgressById(Long id) {
        return progressRepository.findById(id);
    }

    @Override
    public List<Progress> getAllProgresses() {
        return progressRepository.findAll();
    }

    @Override
    public void deleteProgress(Long id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found with id " + id));
        progressRepository.delete(progress);
    }
}
