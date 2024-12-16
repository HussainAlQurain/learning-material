package com.example.skillhub.services.impl;

import com.example.skillhub.domain.Author;
import com.example.skillhub.enums.Rating;
import com.example.skillhub.exceptions.ResourceNotFoundException;
import com.example.skillhub.repositories.AuthorRepository;
import com.example.skillhub.services.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(Author author) {
        author.setCreatedAt(new Date());
        author.setStatus("active"); // default status
        return authorRepository.save(author);
    }

    @Override
    public Author partialUpdate(Long id, Author authorDetails) {
        return authorRepository.findById(id).map(existingAuthor -> {
            Optional.ofNullable(authorDetails.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorDetails.getEmail()).ifPresent(existingAuthor::setEmail);
            Optional.ofNullable(authorDetails.getPhone()).ifPresent(existingAuthor::setPhone);
            Optional.ofNullable(authorDetails.getPassword()).ifPresent(existingAuthor::setPassword);
            Optional.ofNullable(authorDetails.getRole()).ifPresent(existingAuthor::setRole);
            Optional.ofNullable(authorDetails.getStatus()).ifPresent(existingAuthor::setStatus);
            Optional.ofNullable(authorDetails.getRating()).ifPresent(existingAuthor::setRating);
            // Optionally update createdAt if needed
            return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Override
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        authorRepository.delete(author);
    }
}
