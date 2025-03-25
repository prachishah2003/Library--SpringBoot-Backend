package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.*;
import com.ibizabroker.lms.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class responsible for managing book-related operations in the library system.
 * Provides functionality for retrieving book statistics, ratings, and genre-based information.
 * Uses constructor-based dependency injection through Lombok's @RequiredArgsConstructor.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BookService {
    
    /**
     * Repository for performing database operations on books.
     * Automatically injected through constructor due to final modifier and @RequiredArgsConstructor.
     */
    private final BooksRepository bookRepository;

    /**
     * Retrieves the total number of books in the library.
     *
     * @return The total count of books
     */
    public long getTotalBooks() {
        return bookRepository.count();
    }

    /**
     * Retrieves statistics about books grouped by genre.
     * Provides a count of books in each genre category.
     *
     * @return List of maps containing genre names and their corresponding book counts
     */
    public List<Map<String, Object>> getBooksByGenre() {
        return bookRepository.countBooksByGenre();
    }

    /**
     * Retrieves the top 5 highest-rated books in the library.
     * Books are ordered by their average rating in descending order.
     *
     * @return List of the top 5 rated books
     */
    public List<Books> getTopRatedBooks() {
        return bookRepository.findTop5ByOrderByAverageRatingDesc();
    }
}

