package dev.daniel.utility;

import dev.daniel.entity.Author;
import dev.daniel.entity.Book;
import dev.daniel.entity.Category;
import dev.daniel.repository.AuthorRepository;
import dev.daniel.repository.BookRepository;
import dev.daniel.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepo;
    private final CategoryRepository categoryRepo;
    private final BookRepository bookRepo;

    public DataSeeder(AuthorRepository authorRepo, CategoryRepository categoryRepo, BookRepository bookRepo) {
        this.authorRepo = authorRepo;
        this.categoryRepo = categoryRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        // Create 10 authors
        Author[] authors = new Author[10];
        for (int i = 0; i < 10; i++) {
            Author author = new Author();
            author.setName("Author " + (i + 1));
            authorRepo.save(author);
            authors[i] = author;
        }

        // Create 5 categories
        Category[] categories = new Category[5];
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category " + (i + 1));
            categoryRepo.save(category);
            categories[i] = category;
        }

        // Create 50 books
        for (int i = 0; i < 50; i++) {
            Author author = authors[random.nextInt(authors.length)];
            Category category = categories[random.nextInt(categories.length)];
            double rating = 1 + (5 - 1) * random.nextDouble(); // rating 1.0 - 5.0

            Book book = new Book();
            book.setTitle("Book " + (i + 1));
            book.setBookRating(rating);
            book.setAuthor(author);
            book.setCategory(category);

            // maintain bidirectional relationship
            author.addBook(book);
            category.addBook(book);

            bookRepo.save(book);
        }

        System.out.println("50 books inserted with authors and categories.");
    }
}
