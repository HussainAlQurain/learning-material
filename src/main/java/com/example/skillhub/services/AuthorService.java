package com.example.skillhub.services;

import com.example.skillhub.domain.Author;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author createAuthor(Author author);
    Author partialUpdate(Long id, Author author);
    Optional<Author> getAuthorById(Long id);
    List<Author> getAllAuthors();
    void deleteAuthor(Long id);

    Optional<Author> findByEmail(String email);
    boolean existsByEmail(@Email(message = "Email should be valid") @NotBlank(message = "Email is mandatory") String email);
}
