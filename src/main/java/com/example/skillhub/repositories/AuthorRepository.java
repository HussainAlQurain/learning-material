package com.example.skillhub.repositories;

import com.example.skillhub.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByEmail(String email);

    @Query("SELECT COUNT(a) > 0 FROM Author a WHERE a.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
