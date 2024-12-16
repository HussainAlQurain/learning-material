package com.example.skillhub.services;

import com.example.skillhub.domain.Progress;

import java.util.List;
import java.util.Optional;

public interface ProgressService {
    Progress createProgress(Progress progress);
    Progress partialUpdate(Long id, Progress progress);
    Optional<Progress> getProgressById(Long id);
    List<Progress> getAllProgresses();
    void deleteProgress(Long id);
}
