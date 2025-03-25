package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for managing Book entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for searching and retrieving book-related statistics.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
    
    /**
     * Searches for books based on name, author, or genre using case-insensitive partial matching.
     * The search is performed across multiple fields using OR conditions.
     *
     * @param query The search term to match against book name, author, or genre
     * @return List of books matching the search criteria
     */
    @Query("SELECT b FROM Books b WHERE LOWER(b.bookName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(b.bookAuthor) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(b.bookGenre) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Books> searchBooksByNameAuthorGenre(String query);

    /**
     * Finds books by name using case-insensitive partial matching.
     *
     * @param bookName The book name to search for
     * @return List of books with names containing the search term
     */
    List<Books> findByBookNameContainingIgnoreCase(String bookName);

    /**
     * Counts the number of books in each genre category.
     * Returns a list of maps containing genre names and their corresponding book counts.
     *
     * @return List of maps with 'genre' and 'count' keys
     */
    @Query("SELECT b.bookGenre AS genre, COUNT(b) AS count FROM Books b GROUP BY b.bookGenre")
    List<Map<String, Object>> countBooksByGenre();

    /**
     * Retrieves the top 5 books with the highest average ratings.
     *
     * @return List of the 5 highest-rated books
     */
    List<Books> findTop5ByOrderByAverageRatingDesc();

    /**
     * Checks if a book with the given name already exists in the database.
     *
     * @param bookName The book name to check
     * @return true if a book with the name exists, false otherwise
     */
    boolean existsBybookName(String bookName);
}

