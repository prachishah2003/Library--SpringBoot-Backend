package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller handling book-related operations in the library management system.
 * Provides endpoints for CRUD operations on books and book search functionality.
 * Some endpoints require admin privileges.
 *
 * @author codematrix
 * @version 1.0
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/admin")
public class BooksController {

    /**
     * Repository for performing database operations on books.
     */
    @Autowired
    private BooksRepository booksRepository;

    /**
     * Retrieves all books in the library.
     *
     * @return List of all books
     */
    @GetMapping("/books")
    public List<Books> getAllBooks(){
        return booksRepository.findAll();
    }

    /**
     * Retrieves a specific book by its ID.
     * Requires admin privileges.
     *
     * @param id The ID of the book to retrieve
     * @return ResponseEntity containing the book if found
     * @throws NotFoundException if the book is not found
     */
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/books/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id) {
        Books book = booksRepository.findById(id).orElseThrow(() -> new NotFoundException("Book with id "+ id +" does not exist."));
        return ResponseEntity.ok(book);
    }

    /**
     * Creates a new book in the library.
     * Requires admin privileges.
     *
     * @param book The book details to create
     * @return The created book
     */
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/books")
    public Books createBook(@RequestBody Books book) {
        return booksRepository.save(book);
    }

    /**
     * Searches for books by name, author, or genre.
     *
     * @param query The search query string
     * @return ResponseEntity containing the list of matching books
     */
    @GetMapping("/books/search")
    public ResponseEntity<List<Books>> searchBooks(@RequestParam String query) {
        List<Books> books = booksRepository.searchBooksByNameAuthorGenre(query);
        return ResponseEntity.ok(books);
    }

    /**
     * Updates an existing book's details.
     * Requires admin privileges.
     *
     * @param id The ID of the book to update
     * @param bookDetails The new book details
     * @return ResponseEntity containing the updated book
     * @throws NotFoundException if the book is not found
     */
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/books/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable Integer id, @RequestBody Books bookDetails) {
        Books book = booksRepository.findById(id).orElseThrow(() -> new NotFoundException("Book with id "+ id +" does not exist."));

        book.setBookName(bookDetails.getBookName());
        book.setBookAuthor(bookDetails.getBookAuthor());
        book.setBookGenre(bookDetails.getBookGenre());
        book.setNoOfCopies(bookDetails.getNoOfCopies());

        Books updatedBook = booksRepository.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Deletes a book from the library.
     * Requires admin privileges.
     *
     * @param id The ID of the book to delete
     * @return ResponseEntity containing a map indicating successful deletion
     * @throws NotFoundException if the book is not found
     */
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBook(@PathVariable Integer id) {
        Books book = booksRepository.findById(id).orElseThrow(() -> new NotFoundException("Book with id "+ id +" does not exist."));

        booksRepository.delete(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
