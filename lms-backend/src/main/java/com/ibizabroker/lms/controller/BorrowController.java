package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dto.PendingReturnDTO;
import com.ibizabroker.lms.dao.BorrowRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Borrow;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.enums.ReturnReqStatus;
import com.ibizabroker.lms.exceptions.NotFoundException;
import com.ibizabroker.lms.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.util.stream.Collectors;

/**
 * Controller handling book borrowing operations in the library management system.
 * Provides endpoints for borrowing books, returning books, and managing borrow records.
 * Some endpoints require admin privileges.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    /**
     * Repository for performing database operations on borrow records.
     */
    @Autowired
    private BorrowRepository borrowRepository;

    /**
     * Repository for performing database operations on users.
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Service for handling borrow-related operations.
     */
    @Autowired
    private BorrowService borrowService;

    /**
     * Repository for performing database operations on books.
     */
    @Autowired
    private BooksRepository booksRepository;

    /**
     * Retrieves all overdue books.
     * Requires admin privileges.
     *
     * @return ResponseEntity containing list of overdue borrow records
     */
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/overdue-books")
    public ResponseEntity<List<Borrow>> getOverdueBooks() {
        return ResponseEntity.ok(borrowService.getOverdueBooks());
    }

    /**
     * Processes a book borrowing request.
     * Checks user balance, book availability, and existing borrows before processing.
     *
     * @param borrow The borrow request details
     * @return String message indicating success or failure reason
     * @throws NotFoundException if user or book is not found
     */
    @PostMapping
    public String borrowBook(@RequestBody Borrow borrow) {
        Users user = usersRepository.findById(borrow.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + borrow.getUserId()));

        Books book = booksRepository.findById(borrow.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + borrow.getBookId()));

        if (user.getAccountBalance() < 20) {
            return "Insufficient balance. Please add funds.";
        }
        // Check if the user has already borrowed this book and not returned it
        List<ReturnReqStatus> activeStatuses = Arrays.asList(ReturnReqStatus.NONE, ReturnReqStatus.PENDING, ReturnReqStatus.REJECTED);
        boolean alreadyBorrowed = borrowRepository.existsByUserIdAndBookIdAndReturnRequestStatusIn(
                borrow.getUserId(), borrow.getBookId(), activeStatuses
        );

        if (alreadyBorrowed) {
            return "You have already borrowed this book and must return it before borrowing again.";
        }

        if (book.getNoOfCopies() < 1) {
            return "The book \"" + book.getBookName() + "\" is out of stock!";
        }

        // Reduce the available copies
        book.borrowBook();
        booksRepository.save(book);

        // Set issue and due dates
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 7);
        Date dueDate = c.getTime();

        // Save borrow record
        borrow.setIssueDate(currentDate);
        borrow.setDueDate(dueDate);
        borrow.setReturnRequestStatus(ReturnReqStatus.NONE);
        borrow.setReturnStatus("BORROWED");
        user.setAccountBalance(user.getAccountBalance() - 20); // Deduct balance
        usersRepository.save(user);
        borrowRepository.save(borrow);

        return user.getName() + " has borrowed one copy of \"" + book.getBookName() + "\"!";
    }

    /**
     * Retrieves all borrow records.
     *
     * @return List of all borrow records
     */
    @GetMapping
    public List<Borrow> getAllBorrow() {
        return borrowRepository.findAll();
    }

    /**
     * Submits a request to return a borrowed book.
     * Sets the return request status to PENDING for admin approval.
     *
     * @param borrow The borrow record to update
     * @return String message indicating success or failure reason
     * @throws NotFoundException if borrow record is not found
     */
    @PutMapping("/request-return")
    public String requestBookReturn(@RequestBody Borrow borrow) {
        Borrow borrowBook = borrowRepository.findById(borrow.getBorrowId())
                .orElseThrow(() -> new NotFoundException("Borrow record not found"));

        if (borrowBook.getReturnRequestStatus() != ReturnReqStatus.PENDING) {
            borrowBook.setReturnRequestStatus(ReturnReqStatus.PENDING);
            borrowRepository.save(borrowBook);
            return "Return request sent to admin for approval.";
        } else {
            return "Return request is already pending.";
        }
    }

    /**
     * Approves a book return request.
     * Updates book availability and borrow record status.
     * Requires admin privileges.
     *
     * @param borrowId The ID of the borrow record to approve
     * @return String message indicating success
     * @throws NotFoundException if borrow record is not found
     */
    @PutMapping("/admin/approve-return/{borrowId}")
    public String approveReturn(@PathVariable Integer borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new NotFoundException("Borrow record not found"));
        Users user = usersRepository.findById(borrow.getUserId()).get();
        Books book = booksRepository.findById(borrow.getBookId()).get();
        book.returnBook();
        booksRepository.save(book);

        borrow.setReturnDate(new Date());
        borrow.setReturnRequestStatus(ReturnReqStatus.APPROVED);
        borrow.setReturnStatus("RETURNED");

        borrowRepository.save(borrow);

        return "Return request approved. The book has been returned.";
    }

    /**
     * Rejects a book return request.
     * Requires admin privileges.
     *
     * @param borrowId The ID of the borrow record to reject
     * @return String message indicating success
     * @throws NotFoundException if borrow record is not found
     */
    @PutMapping("/admin/reject-return/{borrowId}")
    public String rejectReturn(@PathVariable Integer borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new NotFoundException("Borrow record not found"));

        borrow.setReturnRequestStatus(ReturnReqStatus.REJECTED);
        borrowRepository.save(borrow);

        return "Return request rejected.";
    }

    /**
     * Retrieves all pending return requests.
     * Includes detailed information about users and books.
     * Requires admin privileges.
     *
     * @return List of PendingReturnDTO containing return request details
     * @throws NotFoundException if user or book is not found
     */
    @GetMapping("/admin/pending-returns")
    public List<PendingReturnDTO> getPendingReturnRequests() {
        List<Borrow> pendingRequests = borrowRepository.findByReturnRequestStatus(ReturnReqStatus.PENDING);

        return pendingRequests.stream().map(borrow -> {
            Users user = usersRepository.findById(borrow.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            Books book = booksRepository.findById(borrow.getBookId())
                    .orElseThrow(() -> new NotFoundException("Book not found"));

            return new PendingReturnDTO(
                    borrow.getBorrowId(),
                    user.getUserId(),
                    user.getName(),
                    book.getBookId(),
                    book.getBookName(),
                    borrow.getReturnRequestStatus()
            );
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves all books borrowed by a specific user.
     * Includes detailed information about the borrow records.
     *
     * @param id The ID of the user
     * @return List of maps containing borrow record details
     * @throws NotFoundException if user or book is not found
     */
    @GetMapping("user/{id}")
    public List<Map<String, Object>> booksBorrowedByUser(@PathVariable Integer id) {
        List<Borrow> borrowedBooks = borrowRepository.findByUserId(id);

        return borrowedBooks.stream().map(borrow -> {
            Users user = usersRepository.findById(borrow.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            Books book = booksRepository.findById(borrow.getBookId())
                    .orElseThrow(() -> new NotFoundException("Book not found"));

            Map<String, Object> bookDetails = new HashMap<>();
            bookDetails.put("borrowId", borrow.getBorrowId());
            bookDetails.put("userId", user.getUserId());
            bookDetails.put("userName", user.getName());
            bookDetails.put("bookId", book.getBookId());
            bookDetails.put("bookName", book.getBookName());
            bookDetails.put("dueDate", borrow.getDueDate());
            bookDetails.put("issueDate", borrow.getIssueDate());
            bookDetails.put("returnDate", borrow.getReturnDate());
            bookDetails.put("returnRequestStatus", borrow.getReturnRequestStatus());

            return bookDetails;
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves the borrow history of a specific book.
     *
     * @param id The ID of the book
     * @return List of borrow records for the book
     */
    @GetMapping("book/{id}")
    public List<Borrow> bookBorrowHistory(@PathVariable Integer id) {
        return borrowRepository.findByBookId(id);
    }


//    @Autowired
//    private EntityManager entityManager;
//
//    @PostMapping
//    public Borrow borrowBook(@RequestBody Borrow borrow) {
//        borrowRepository.save(borrow);
//        Books book = booksRepository.findById(borrow.getBOOKID()).orElseThrow(() -> new NotFoundException("Book not found."));
//        if(book.getNoOfCopies()-1 < 0) {
//            throw new IllegalStateException("There are no available books.");
//        }
//        book.borrowBook();
//        booksRepository.save(book);
//
//        return borrow;
//    }
//
//    @GetMapping
//    public List<Borrow> getAllBorrow() {
//        return borrowRepository.findAll();
//    }
//
//    @PutMapping
//    public Borrow returnBook(@RequestBody Borrow borrow) {
//        borrowRepository.save(borrow);
//        Books book = booksRepository.findById(borrow.getBOOKID()).orElseThrow(() -> new NotFoundException("Book not found."));
//        book.returnBook();
//        booksRepository.save(book);
//
//        Date currentDate = new Date(new java.util.Date().getTime());
//        borrow.setReturnDate(currentDate);
//        return borrow;
//    }
//
//    @GetMapping("user/{id}")
//    public List<Books> booksBorrowedByUser(@PathVariable Integer id) {
//        Query q = entityManager.createNativeQuery("SELECT * FROM BOOKS AS B, BORROW AS L WHERE B.book_id = L.BOOKID AND L.USERID = " + id);
//        List<Books> borrowedBooks = q.getResultList();
//        return borrowedBooks;
//    }
//
//    @GetMapping("book/{id}")
//    public List<Users> bookBorrowHistory(@PathVariable Integer id) {
//        Query q = entityManager.createNativeQuery("SELECT * FROM USERS AS U, BORROW AS L WHERE U.user_id = L.USERID AND L.BOOKID = " + id);
//        List<Users> usersList = q.getResultList();
//        return usersList;
//    }

//    @PostMapping
//    public Borrow borrowBook(@RequestBody Borrow borrow) {
//        borrow(borrow.getBorrowId(), borrow.getUser().getUserId(), borrow.getBook().getBookId());
//        return borrow;
//    }
//
//    @GetMapping
//    public List<Borrow> getAllBorrow() {
//        return borrowRepository.findAll();
//    }
//
//    @PutMapping
//    public Borrow returnBook(@RequestBody Borrow borrow) {
//        Books book = booksRepository.findById(borrow.getBook().getBookId()).orElseThrow(() -> new NotFoundException("Book not found."));
//        book.returnBook();
//        booksRepository.save(book);
//
//        Date currentDate = new Date(new java.util.Date().getTime());
//        borrow.setReturnDate(currentDate);
//        return borrowRepository.save(borrow);
//    }
//
//    @GetMapping("user/{id}")
//    public List<Books> booksBorrowedByUser(@PathVariable Integer id) {
//        Users user = usersRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found."));
//        return user.getBooks();
//    }
//
//    @GetMapping("book/{id}")
//    public List<Users> bookBorrowHistory(@PathVariable Integer id) {
//        Books book = booksRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found."));
//        return book.getUsers();
//    }
//
//    public void borrow(Integer borrowId, Integer userId, Integer bookId) {
//        Users user = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found."));
//        if(user.getBooks().stream().anyMatch(book -> Objects.equals(book.getBookId(), bookId))) {
//            throw new IllegalStateException("User already borrowed the book");
//        }
//
//        Books book = booksRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found."));
//        if(book.getNoOfCopies()-1 < 0) {
//            throw new IllegalStateException("There are no available books.");
//        }
//
//        book.getUsers().add(user);
//        book.setNoOfCopies(book.getNoOfCopies()-1);
//        booksRepository.save(book);
//
//        user.getBooks().add(book);
//        usersRepository.save(user);
//    }

}
