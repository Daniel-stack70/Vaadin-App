package dev.daniel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "book_rating")
    private Double bookRating;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;



    public Book(String title, Double rating, Author managedAuthor, Category managedCategory) {
        this.title = title;
        this.bookRating = rating;
        this.author = managedAuthor;
        this.category = managedCategory;
    }
}
