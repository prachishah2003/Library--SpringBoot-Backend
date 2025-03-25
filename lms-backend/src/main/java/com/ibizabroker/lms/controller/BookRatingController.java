package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.service.BookRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling book rating operations in the library management system.
 * Provides endpoints for submitting and managing book ratings.
 *
 * @author codematrix
 * @version 1.0
 */
@RestController
@RequestMapping("/api/ratings")
@CrossOrigin("http://localhost:4200/")
public class BookRatingController {
    /**
     * Service responsible for handling book rating operations.
     */
    private final BookRatingService bookRatingService;

    /**
     * Constructs a new BookRatingController with the specified service.
     *
     * @param bookRatingService The service to handle book rating operations
     */
    public BookRatingController(BookRatingService bookRatingService) {
        this.bookRatingService = bookRatingService;
    }

    /**
     * Submits a rating for a specific book.
     *
     * @param bookId The ID of the book being rated
     * @param rating The rating value to submit (typically 1-5)
     * @return ResponseEntity containing a success message
     */
    @PostMapping("/submit")
    public ResponseEntity<String> submitRating(@RequestParam int bookId, @RequestParam int rating) {
        bookRatingService.saveRating(bookId, rating);
        return ResponseEntity.ok("Rating submitted successfully");
    }
}


