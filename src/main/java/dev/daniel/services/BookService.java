package dev.daniel.services;

import dev.daniel.repository.AuthorRepository;
import dev.daniel.repository.BookRepository;
import dev.daniel.repository.CategoryRepository;
import dev.daniel.entity.Author;
import dev.daniel.entity.Book;
import dev.daniel.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }


    @Transactional
    public void addBook(String title, Double rating, Author author, Category category) {
        Author managedAuthor = authorRepository.findById(author.getId())
                .orElseThrow();
        Category managedCategory = categoryRepository.findById(category.getId())
                .orElseThrow();

        Book book = new Book(title, rating, managedAuthor, managedCategory);

        managedAuthor.addBook(book);
        managedCategory.addBook(book);

        bookRepository.save(book);
    }


    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
