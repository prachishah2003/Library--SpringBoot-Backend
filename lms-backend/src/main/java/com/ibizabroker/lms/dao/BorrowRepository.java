package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Borrow;
import com.ibizabroker.lms.enums.ReturnReqStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for managing Borrow entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for retrieving borrowing statistics and managing book loans.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {
    
    /**
     * Finds all borrow records for a specific user.
     *
     * @param userId The ID of the user
     * @return List of borrow records for the user
     */
    List<Borrow> findByUserId(Integer userId);

    /**
     * Finds all borrow records for a specific book.
     *
     * @param bookId The ID of the book
     * @return List of borrow records for the book
     */
    List<Borrow> findByBookId(Integer bookId);

    /**
     * Checks if a user has any borrow records.
     *
     * @param userId The ID of the user to check
     * @return true if the user has any borrows, false otherwise
     */
    boolean existsByUserId(Integer userId);

    /**
     * Finds all borrow records with a specific return request status.
     *
     * @param status The return request status to filter by
     * @return List of borrow records with the specified status
     */
    List<Borrow> findByReturnRequestStatus(ReturnReqStatus status);

    /**
     * Counts the total number of books that have not been returned.
     * Excludes books with 'APPROVED' return request status.
     *
     * @return Count of unreturned books
     */
    @Query("SELECT COUNT(b) FROM Borrow b WHERE b.returnRequestStatus <> 'APPROVED'")
    long countUnreturnedBooks();

    /**
     * Counts borrow records with a specific return request status.
     *
     * @param status The return request status to count
     * @return Count of borrow records with the specified status
     */
    long countByReturnRequestStatus(ReturnReqStatus status);

    /**
     * Finds the most active users based on their borrowing history.
     * Returns username and borrow count, ordered by borrow count in descending order.
     *
     * @return List of maps containing username and borrowCount
     */
    @Query(value = "SELECT u.username AS username, COUNT(br.user_id) AS borrowCount FROM borrow br JOIN users u ON br.user_id = u.user_id GROUP BY u.username ORDER BY borrowCount DESC", nativeQuery = true)
    List<Map<String, Object>> findMostActiveUsers();

    /**
     * Finds the most borrowed book genres.
     * Returns genre and borrow count, ordered by count in descending order.
     *
     * @return List of maps containing genre and count
     */
    @Query(value = "SELECT b.book_genre AS genre, COUNT(br.book_id) AS count FROM borrow br JOIN books b ON br.book_id = b.book_id GROUP BY b.book_genre ORDER BY count DESC", nativeQuery = true)
    List<Map<String, Object>> findMostBorrowedGenres();

    /**
     * Counts the number of overdue books excluding those with a specific status.
     *
     * @param status The return request status to exclude
     * @return Count of overdue books
     */
    @Query("SELECT COUNT(br) FROM Borrow br WHERE br.dueDate < CURRENT_DATE AND br.returnRequestStatus <> :status")
    long countOverdueBooks(ReturnReqStatus status);

    /**
     * Finds all overdue books excluding those with a specific status.
     *
     * @param status The return request status to exclude
     * @return List of overdue borrow records
     */
    @Query("SELECT b FROM Borrow b WHERE b.dueDate < CURRENT_DATE AND b.returnRequestStatus <> :status")
    List<Borrow> findOverdueBooks(ReturnReqStatus status);

    /**
     * Finds the most frequently borrowed books.
     * Returns book title and borrow count, ordered by count in descending order.
     *
     * @return List of maps containing title and count
     */
    @Query(value = "SELECT b.book_name AS title, COUNT(br.book_id) AS count FROM borrow br JOIN books b ON br.book_id = b.book_id GROUP BY b.book_name ORDER BY count DESC", nativeQuery = true)
    List<Map<String, Object>> findMostBorrowedBooks();

    /**
     * Finds borrow records that are due before a specific date and have a specific return status.
     *
     * @param dueDate The date to check against
     * @param returnStatus The return status to filter by
     * @return List of matching borrow records
     */
    List<Borrow> findByDueDateBeforeAndReturnStatus(Date dueDate, String returnStatus);

    /**
     * Finds the number of books borrowed per month.
     * Returns month number and borrow count, ordered by month.
     *
     * @return List of maps containing month and count
     */
    @Query(value = "SELECT EXTRACT(MONTH FROM issue_date) AS month, COUNT(*) AS count FROM borrow GROUP BY month ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> findBooksBorrowedPerMonth();

    /**
     * Checks if a user has borrowed a specific book with any of the specified return request statuses.
     *
     * @param userId The ID of the user
     * @param bookId The ID of the book
     * @param statuses List of return request statuses to check
     * @return true if a matching borrow record exists, false otherwise
     */
    boolean existsByUserIdAndBookIdAndReturnRequestStatusIn(Integer userId, Integer bookId, List<ReturnReqStatus> statuses);
}
