package com.example.skillhub.services.impl;

import com.example.skillhub.domain.Section;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.repositories.SectionRepository;
import com.example.skillhub.services.SectionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Section createSection(Section section) {
        section.setCreatedAt(new Date());
        return sectionRepository.save(section);
    }

    @Override
    public Section partialUpdate(Long id, Section sectionDetails) {
        return sectionRepository.findById(id).map(existingSection -> {
            Optional.ofNullable(sectionDetails.getLesson()).ifPresent(existingSection::setLesson);
            Optional.ofNullable(sectionDetails.getTitle()).ifPresent(existingSection::setTitle);
            Optional.ofNullable(sectionDetails.getContentType()).ifPresent(existingSection::setContentType);
            Optional.ofNullable(sectionDetails.getContentUrl()).ifPresent(existingSection::setContentUrl);
            Optional.ofNullable(sectionDetails.getContentText()).ifPresent(existingSection::setContentText);
            // Update createdAt if needed
            return sectionRepository.save(existingSection);
        }).orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + id));
    }

    @Override
    public Optional<Section> getSectionById(Long id) {
        return sectionRepository.findById(id);
    }

    @Override
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @Override
    public void deleteSection(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id " + id));
        sectionRepository.delete(section);
    }
}
