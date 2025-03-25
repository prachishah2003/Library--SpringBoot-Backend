package com.ibizabroker.lms.service;
import com.ibizabroker.lms.dao.BorrowRepository;

import com.ibizabroker.lms.entity.Borrow;
import com.ibizabroker.lms.enums.ReturnReqStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for managing book borrowing operations in the library system.
 * Handles book borrowing, return requests, and provides various borrowing-related statistics.
 * Uses constructor-based dependency injection through Lombok's @RequiredArgsConstructor.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BorrowService {
    
    /**
     * Repository for performing database operations on borrow records.
     */
    @Autowired
    private BorrowRepository borrowRepository;

    /**
     * Processes a book borrowing request.
     * Checks if the user has already borrowed the book and creates a new borrow record if not.
     *
     * @param userId The ID of the user borrowing the book
     * @param bookId The ID of the book being borrowed
     * @return Success message if the book is borrowed successfully
     * @throws IllegalStateException if the user has already borrowed the book
     */
    public String borrowBook(Integer userId, Integer bookId) {
        // Check if the user already has this book and has not returned it
        List<ReturnReqStatus> activeStatuses = Arrays.asList(ReturnReqStatus.NONE, ReturnReqStatus.PENDING, ReturnReqStatus.REJECTED);
        boolean alreadyBorrowed = borrowRepository.existsByUserIdAndBookIdAndReturnRequestStatusIn(userId, bookId, activeStatuses);

        if (alreadyBorrowed) {
            throw new IllegalStateException("You have already borrowed this book and must return it before borrowing again.");
        }

        // Proceed with borrowing the book
        Borrow borrow = new Borrow();
        borrow.setUserId(userId);
        borrow.setBookId(bookId);
        borrow.setIssueDate(new Date());
        borrow.setDueDate(calculateDueDate());
        borrow.setReturnRequestStatus(ReturnReqStatus.PENDING);

        borrowRepository.save(borrow);
        return "Book borrowed successfully!";
    }

    /**
     * Calculates the due date for a borrowed book.
     * Sets the due date to 14 days from the current date.
     *
     * @return Date object representing the calculated due date
     */
    private Date calculateDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14); // 2-week borrowing period
        return calendar.getTime();
    }

    /**
     * Gets the total number of books currently borrowed.
     *
     * @return Count of books that have not been returned
     */
    public long getTotalBorrowedBooks() {
        return borrowRepository.countUnreturnedBooks();
    }

    /**
     * Retrieves a list of all overdue books.
     * Only includes books with approved return status.
     *
     * @return List of overdue borrow records
     */
    public List<Borrow> getOverdueBooks() {
        return borrowRepository.findOverdueBooks(ReturnReqStatus.APPROVED);
    }

    /**
     * Retrieves statistics about the most active library users.
     * Based on the number of books borrowed by each user.
     *
     * @return List of maps containing user activity statistics
     */
    public List<Map<String, Object>> getMostActiveUsers() {
        return borrowRepository.findMostActiveUsers();
    }

    /**
     * Retrieves statistics about the most frequently borrowed book genres.
     *
     * @return List of maps containing genre borrowing statistics
     */
    public List<Map<String, Object>> getMostBorrowedGenres() {
        return borrowRepository.findMostBorrowedGenres();
    }

    /**
     * Gets the total number of overdue books.
     * Only counts books with approved return status.
     *
     * @return Count of overdue books
     */
    public long getTotalOverdueBooks() {
        long overdueBooks = borrowRepository.countOverdueBooks(ReturnReqStatus.APPROVED);

        return overdueBooks;
    }

    /**
     * Retrieves statistics about the most frequently borrowed books.
     *
     * @return List of maps containing book borrowing statistics
     */
    public List<Map<String, Object>> getMostBorrowedBooks() {
        return borrowRepository.findMostBorrowedBooks();
    }

    /**
     * Retrieves monthly statistics about book borrowings.
     * Groups borrowings by month to show borrowing trends.
     *
     * @return List of maps containing monthly borrowing statistics
     */
    public List<Map<String, Object>> getBooksBorrowedPerMonth() {
        return borrowRepository.findBooksBorrowedPerMonth();
    }
}

