package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.RequestedBookRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.RequestedBook;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

/**
 * Controller handling book request operations in the library management system.
 * Provides endpoints for users to request new books and for admins to manage these requests.
 * Different endpoints have different role-based access requirements.
 *
 * @author codematrix
 * @version 1.0
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/requested-books")
public class RequestedBookController {

    /**
     * Repository for performing database operations on book requests.
     */
    @Autowired
    private RequestedBookRepository repository;

    /**
     * Repository for performing database operations on books.
     */
    @Autowired
    private BooksRepository booksRepository;

    /**
     * Repository for performing database operations on users.
     */
    @Autowired
    private UsersRepository userRepository;

    /**
     * Submits a request for a new book to be added to the library.
     * Only accessible to users with the 'User' role.
     * Checks if the book already exists before creating the request.
     *
     * @param bookRequest The book request details
     * @param authentication The authentication object containing user details
     * @return ResponseEntity containing either:
     *         - The saved request (200 OK)
     *         - Error message if book exists (400 Bad Request)
     * @throws NotFoundException if the user is not found
     */
    @PreAuthorize("hasRole('User')")
    @PostMapping
    public ResponseEntity<?> requestBook(@RequestBody RequestedBook bookRequest, Authentication authentication) {
        String username = authentication.getName(); // Get currently logged-in user

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        boolean bookExists = booksRepository.existsBybookName(bookRequest.getBookName());
        if (bookExists) {
            return ResponseEntity.badRequest().body("This book already exists in the library.");
        }
        RequestedBook newRequest = new RequestedBook();
        newRequest.setBookName(bookRequest.getBookName());
        newRequest.setRequestedBy(user); // Assign the logged-in user
        newRequest.setRequestedAt(new java.util.Date());

        RequestedBook savedRequest = repository.save(newRequest);
        return ResponseEntity.ok(savedRequest);
    }

    /**
     * Retrieves all book requests.
     * Only accessible to users with the 'Admin' role.
     *
     * @return List of all book requests with associated user information
     */
    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public List<RequestedBook> getAllRequests() {
        return repository.findAllWithUsers();
    }

    /**
     * Deletes a specific book request.
     * Only accessible to users with the 'Admin' role.
     *
     * @param id The ID of the book request to delete
     * @return ResponseEntity containing a success message
     */
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok("Book request deleted");
    }
}

