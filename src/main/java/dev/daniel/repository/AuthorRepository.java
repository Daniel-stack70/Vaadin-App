package dev.daniel.repository;

import dev.daniel.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

    String countByNameContainingIgnoreCase(String filter);

    List<Author> findByNameContainingIgnoreCase(String filter);
}
