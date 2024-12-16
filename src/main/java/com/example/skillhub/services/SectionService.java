package com.example.skillhub.services;

import com.example.skillhub.domain.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    Section createSection(Section section);
    Section partialUpdate(Long id, Section section);
    Optional<Section> getSectionById(Long id);
    List<Section> getAllSections();
    void deleteSection(Long id);
}
