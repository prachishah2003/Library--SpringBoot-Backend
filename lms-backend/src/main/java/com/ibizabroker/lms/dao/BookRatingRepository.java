package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing BookRating entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for retrieving and managing book ratings.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {
    
    /**
     * Finds all ratings for a specific book.
     * Uses the book's ID to retrieve associated ratings.
     *
     * @param bookId The ID of the book to find ratings for
     * @return List of ratings associated with the book
     */
    List<BookRating> findByBook_BookId(int bookId);
}

