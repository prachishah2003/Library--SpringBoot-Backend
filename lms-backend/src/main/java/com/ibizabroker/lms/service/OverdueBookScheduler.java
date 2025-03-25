package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BorrowRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.Borrow;
import com.ibizabroker.lms.entity.Users;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Service class responsible for managing overdue book operations in the library system.
 * Runs scheduled tasks to check for overdue books and apply fines automatically.
 * Uses Spring's @Scheduled annotation for automated execution.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
public class OverdueBookScheduler {

    /**
     * Repository for accessing and updating borrow records.
     */
    private final BorrowRepository borrowRepository;

    /**
     * Repository for accessing and updating user information.
     */
    private final UsersRepository userRepository;

    /**
     * Constructs a new OverdueBookScheduler with required repositories.
     *
     * @param borrowRepository Repository for borrow operations
     * @param userRepository Repository for user operations
     */
    public OverdueBookScheduler(BorrowRepository borrowRepository, UsersRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
    }

    /**
     * Scheduled task that runs daily at midnight to check for overdue books and apply fines.
     * For each overdue book:
     * 1. Calculates the number of overdue days
     * 2. Calculates the fine amount (₹10 per day)
     * 3. Deducts the fine from user's account balance if sufficient
     * 4. Updates the book status to OVERDUE
     * 
     * This method is transactional to ensure data consistency.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    @Transactional
    public void markOverdueBooksAndDeductFine() {
        Date today = new Date();

        // Fetch all books that are overdue and still in "BORROWED" status
        List<Borrow> overdueBooks = borrowRepository.findByDueDateBeforeAndReturnStatus(today, "BORROWED");

        for (Borrow borrow : overdueBooks) {
            Date dueDate = borrow.getDueDate();
            long overdueDays = calculateOverdueDays(dueDate, today);
            double fine = overdueDays * 10; // ₹10 per day

            // Fetch user safely
            Optional<Users> optionalUser = userRepository.findById(borrow.getUserId());
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();

                // Deduct fine if balance is sufficient
                if (user.getAccountBalance() >= fine) {
                    user.setAccountBalance(user.getAccountBalance() - fine);
                    borrow.setFine(fine);
                    borrow.setReturnStatus("OVERDUE");

                    userRepository.save(user);
                    borrowRepository.save(borrow);
                    System.out.println("Fine of ₹" + fine + " deducted from " + user.getUsername());
                } else {
                    System.out.println("Insufficient balance for " + user.getUsername() + " to deduct fine of ₹" + fine);
                }
            } else {
                System.out.println("User with ID " + borrow.getUserId() + " not found!");
            }
        }
    }

    /**
     * Calculates the number of days between the due date and today.
     * Uses TimeUnit conversion to ensure accurate day calculation.
     *
     * @param dueDate The date when the book was due
     * @param today The current date
     * @return Number of days the book is overdue
     */
    private long calculateOverdueDays(Date dueDate, Date today) {
        long diffInMillies = today.getTime() - dueDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
