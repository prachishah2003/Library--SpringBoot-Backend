package com.ibizabroker.lms.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a book rating in the library management system.
 * This class maps to the "book_ratings" table in the database and stores
 * user ratings for books along with the timestamp of when the rating was created.
 *
 * @author codematrix
 * @version 1.0
 */
@Data
@Entity
@Table(name = "book_ratings")
public class BookRating {

    /**
     * Unique identifier for the rating record.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_rating_seq")
    @SequenceGenerator(name = "book_rating_seq", sequenceName = "book_rating_seq", allocationSize = 1)
    private Long id;

    /**
     * The book being rated.
     * Implements many-to-one relationship with the Books entity.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    /**
     * The user who gave the rating.
     * Implements many-to-one relationship with the Users entity.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /**
     * Rating value given to the book (out of 5 stars).
     */
    private int rating; // Rating out of 5 stars

    /**
     * Timestamp when the rating was created.
     * This field cannot be null and cannot be updated after creation.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically sets the creation timestamp when a new rating is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
