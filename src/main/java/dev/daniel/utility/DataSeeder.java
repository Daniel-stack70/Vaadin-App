package dev.daniel.utility;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.csv.CSVReader;
import dev.daniel.entity.Author;
import dev.daniel.entity.Book;
import dev.daniel.entity.Category;
import dev.daniel.repository.AuthorRepository;
import dev.daniel.repository.BookRepository;
import dev.daniel.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;



@Component
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepo;
    private final CategoryRepository categoryRepo;
    private final BookRepository bookRepo;

    Random random = new Random();

    public DataSeeder(AuthorRepository authorRepo, CategoryRepository categoryRepo, BookRepository bookRepo) {
        this.authorRepo = authorRepo;
        this.categoryRepo = categoryRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("books_list.csv");
        if (is == null) {
            throw new FileNotFoundException("books_list.csv not found in resources");
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            boolean first = true;

            ICommonsList<String> row;
            while ((row = reader.readNext()) != null) {
                String title = row.get(0);
                String genre = row.get(1);
                String authorName = row.get(2);

                Author author = authorRepo.findByName(authorName)
                        .orElseGet(() -> {
                            Author a = new Author();
                            a.setName(authorName);
                            return authorRepo.save(a);
                        });

                Category category = categoryRepo.findByName(genre)
                        .orElseGet(() -> {
                            Category c = new Category();
                            c.setName(genre);
                            return categoryRepo.save(c);
                        });

                Book book = new Book();
                book.setTitle(title);
                double rating;
                switch (genre) {
                    case "Philosophy":
                        rating = 9 + random.nextDouble();
                        break;
                    case "Comedy":
                        rating = 5 + 2 * random.nextDouble();
                        break;
                    case "SciFi":
                        rating = 4 + 3 * random.nextDouble();
                        break;
                    case "Horror":
                        rating = 7 + random.nextDouble();
                        break;
                    case "Action":
                        rating = 3 + 5 * random.nextDouble();
                        break;
                    default:
                        rating = 5.0;
                }

                book.setBookRating(rating);
                book.setAuthor(author);
                book.setCategory(category);

                author.addBook(book);
                category.addBook(book);

                bookRepo.save(book);
            }
        }

        System.out.println("Books inserted from CSV.");
    }
}


