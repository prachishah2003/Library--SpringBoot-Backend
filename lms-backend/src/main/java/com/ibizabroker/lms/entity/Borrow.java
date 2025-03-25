package com.ibizabroker.lms.entity;

import com.ibizabroker.lms.enums.ReturnReqStatus;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Entity class representing a book borrowing record in the library management system.
 * This class maps to the "borrow" table in the database and tracks all book borrowing
 * activities including issue dates, return dates, and fines.
 *
 * @author codematrix
 * @version 1.0
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "borrow") // Lowercase table name for PostgreSQL compatibility
public class Borrow {

    /**
     * Unique identifier for the borrowing record.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrow_seq")
    @SequenceGenerator(name = "borrow_seq", sequenceName = "borrow_seq", allocationSize = 1)
    private Integer borrowId;

    /**
     * ID of the borrowed book.
     * This field cannot be null.
     */
    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    /**
     * ID of the user who borrowed the book.
     * This field cannot be null.
     */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /**
     * Current status of the borrowed book.
     * Possible values: BORROWED, OVERDUE, RETURNED
     */
    @Column(nullable = true)
    private String returnStatus;  // BORROWED, OVERDUE, RETURNED

    /**
     * Fine amount charged for late return or other violations.
     */
    @Column(nullable = true)
    private double fine;

    /**
     * Date and time when the book was issued to the user.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;

    /**
     * Date and time when the book was returned by the user.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    /**
     * Date and time by which the book should be returned.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    /**
     * Status of the return request.
     * Default value is PENDING.
     */
    @Enumerated(EnumType.STRING)
    private ReturnReqStatus returnRequestStatus = ReturnReqStatus.PENDING;

}
