package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.Books;

import com.ibizabroker.lms.service.BookService;
import com.ibizabroker.lms.service.BorrowService;
import com.ibizabroker.lms.service.RequestService;
import com.ibizabroker.lms.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller handling statistical operations in the library management system.
 * Provides endpoints for retrieving various statistics and analytics about
 * books, users, borrowings, and other library activities.
 * All endpoints are accessible only to administrators.
 *
 * @author codematrix
 * @version 1.0
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    /**
     * Service for handling book-related operations.
     */
    private final BookService bookService;

    /**
     * Service for handling user-related operations.
     */
    private final UserService userService;

    /**
     * Service for handling borrow-related operations.
     */
    private final BorrowService borrowService;

    /**
     * Service for handling request-related operations.
     */
    private final RequestService requestService;

    /**
     * Retrieves summary statistics of the library system.
     * Includes total books, users, borrowed books, overdue books,
     * requested books, and return requests.
     *
     * @return ResponseEntity containing a map of summary statistics
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummaryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", bookService.getTotalBooks());
        stats.put("totalUsers", userService.getTotalUsers());
        stats.put("borrowedBooks", borrowService.getTotalBorrowedBooks());
        stats.put("overdueBooks", borrowService.getTotalOverdueBooks());
        stats.put("requestedBooks", requestService.getTotalRequestedBooks());
        stats.put("returnRequests", requestService.getTotalReturnRequests());
        return ResponseEntity.ok(stats);
    }

    /**
     * Retrieves statistics about books grouped by genre.
     *
     * @return ResponseEntity containing a list of genre statistics
     */
    @GetMapping("/books-by-genre")
    public ResponseEntity<List<Map<String, Object>>> getBooksByGenre() {
        return ResponseEntity.ok(bookService.getBooksByGenre());
    }

    /**
     * Retrieves a list of most active users based on borrowing activity.
     *
     * @return ResponseEntity containing a list of user activity statistics
     */
    @GetMapping("/most-active-users")
    public ResponseEntity<List<Map<String, Object>>> getMostActiveUsers() {
        return ResponseEntity.ok(borrowService.getMostActiveUsers());
    }

    /**
     * Retrieves statistics about most frequently borrowed book genres.
     *
     * @return ResponseEntity containing a list of genre borrowing statistics
     */
    @GetMapping("/most-borrowed-genres")
    public ResponseEntity<List<Map<String, Object>>> getMostBorrowedGenres() {
        return ResponseEntity.ok(borrowService.getMostBorrowedGenres());
    }

    /**
     * Retrieves a list of books with highest ratings.
     *
     * @return ResponseEntity containing a list of top-rated books
     */
    @GetMapping("/top-rated-books")
    public ResponseEntity<List<Books>> getTopRatedBooks() {
        return ResponseEntity.ok(bookService.getTopRatedBooks());
    }

    /**
     * Retrieves statistics about most frequently borrowed books.
     *
     * @return ResponseEntity containing a list of book borrowing statistics
     */
    @GetMapping("/most-borrowed-books")
    public ResponseEntity<List<Map<String, Object>>> getMostBorrowedBooks() {
        return ResponseEntity.ok(borrowService.getMostBorrowedBooks());
    }

    /**
     * Retrieves monthly book borrowing statistics.
     *
     * @return ResponseEntity containing a list of monthly borrowing statistics
     */
    @GetMapping("/books-borrowed-per-month")
    public ResponseEntity<List<Map<String, Object>>> getBooksBorrowedPerMonth() {
        return ResponseEntity.ok(borrowService.getBooksBorrowedPerMonth());
    }
}

