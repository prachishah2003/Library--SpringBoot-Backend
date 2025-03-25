package com.ibizabroker.lms.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * Entity class representing a book in the library management system.
 * This class maps to the "books" table in the database and contains all the necessary
 * information about a book including its details and availability.
 *
 * @author codematrix
 * @version 1.0
 */
@Data
@Entity
@Table(name = "books") // Table name in lowercase for PostgreSQL compatibility
public class Books {

    /**
     * Unique identifier for the book.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_seq")
    @SequenceGenerator(name = "books_seq", sequenceName = "books_seq", allocationSize = 1)
    private Integer bookId;

    /**
     * The title of the book.
     * This field cannot be null.
     */
    @Column(name = "book_name", nullable = false)
    private String bookName;

    /**
     * The author of the book.
     * This field cannot be null.
     */
    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    /**
     * The genre or category of the book.
     */
    @Column(name = "book_genre")
    private String bookGenre;

    /**
     * URL to the book's cover image.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Number of copies available in the library.
     * This field cannot be null.
     */
    @Column(name = "no_of_copies", nullable = false)
    private Integer noOfCopies;

    /**
     * Average rating of the book based on user reviews.
     * Default value is 0.0.
     */
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    /**
     * Decrements the number of available copies when a book is borrowed.
     * Only decrements if there are copies available.
     */
    public void borrowBook() {
        if (this.noOfCopies > 0) {
            this.noOfCopies--;
        }
    }

    /**
     * Increments the number of available copies when a book is returned.
     */
    public void returnBook() {
        this.noOfCopies++;
    }
}
