package com.example.skillhub.services;

import com.example.skillhub.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author createAuthor(Author author);
    Author partialUpdate(Long id, Author author);
    Optional<Author> getAuthorById(Long id);
    List<Author> getAllAuthors();
    void deleteAuthor(Long id);
}
