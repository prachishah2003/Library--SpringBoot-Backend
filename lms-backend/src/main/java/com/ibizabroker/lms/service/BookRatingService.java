package com.ibizabroker.lms.service;

import com.ibizabroker.lms.entity.BookRating;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.dao.BookRatingRepository;
import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing book ratings in the library system.
 * Handles rating submission, average rating calculation, and rating updates.
 * Integrates with Spring Security for user authentication.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
public class BookRatingService {
    
    /**
     * Repository for performing database operations on book ratings.
     */
    private final BookRatingRepository bookRatingRepository;

    /**
     * Repository for accessing and updating book information.
     */
    private final BooksRepository booksRepository;

    /**
     * Repository for accessing user information.
     */
    private final UsersRepository usersRepository;

    /**
     * Constructs a new BookRatingService with required repositories.
     *
     * @param bookRatingRepository Repository for rating operations
     * @param booksRepository Repository for book operations
     * @param usersRepository Repository for user operations
     */
    public BookRatingService(BookRatingRepository bookRatingRepository, BooksRepository booksRepository, UsersRepository usersRepository) {
        this.bookRatingRepository = bookRatingRepository;
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * Retrieves the username of the currently authenticated user.
     * Uses Spring Security's SecurityContextHolder to access authentication details.
     *
     * @return The username of the authenticated user, or null if not authenticated
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    /**
     * Saves a new rating for a book from the currently authenticated user.
     * Creates a new rating record and updates the book's average rating.
     *
     * @param bookId The ID of the book being rated
     * @param rating The rating value to save
     * @throws RuntimeException if user is not authenticated, user not found, or book not found
     */
    public void saveRating(int bookId, int rating) {
        String username = getCurrentUsername();
        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        BookRating bookRating = new BookRating();
        bookRating.setBook(book);
        bookRating.setUser(user);
        bookRating.setRating(rating);
        bookRatingRepository.save(bookRating);

        // Calculate the new average rating
        updateAverageRating(book);
    }

    /**
     * Updates the average rating of a book based on all its ratings.
     * Calculates the average and updates the book entity in the database.
     *
     * @param book The book whose average rating needs to be updated
     */
    private void updateAverageRating(Books book) {
        List<BookRating> ratings = bookRatingRepository.findByBook_BookId(book.getBookId());
        double avgRating = ratings.stream()
                .mapToInt(BookRating::getRating)
                .average()
                .orElse(0.0);
        book.setAverageRating(avgRating);
        booksRepository.save(book);
    }
}


