package dev.daniel.services;

import dev.daniel.repository.AuthorRepository;
import dev.daniel.entity.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Author saveAuthor(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }

        Author author = new Author();
        author.setName(name.trim());

        return authorRepository.save(author);
    }

    public List<Author> findAuthors(String filter, int offset, int limit) {
        if (filter == null || filter.isBlank()) {
            return authorRepository.findAll()
                    .stream()
                    .toList();
        }

        return authorRepository.findByNameContainingIgnoreCase(filter)
                .stream()
                .toList();
    }

    public int countAuthors(String filter) {
        if (filter == null || filter.isBlank()) {
            return (int) authorRepository.count();
        }
        return Integer.parseInt(authorRepository.countByNameContainingIgnoreCase(filter));
    }
}
