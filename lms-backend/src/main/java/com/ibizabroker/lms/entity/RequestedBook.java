package com.ibizabroker.lms.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * Entity class representing a book request in the library management system.
 * This class maps to the "requested_books" table in the database and tracks
 * books that users have requested to be added to the library.
 *
 * @author ibizabroker
 * @version 1.0
 */
@Data
@Entity
@Table(name = "requested_books") // Lowercase for PostgreSQL compatibility
public class RequestedBook {

    /**
     * Unique identifier for the request record.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requested_books_seq")
    @SequenceGenerator(name = "requested_books_seq", sequenceName = "requested_books_seq", allocationSize = 1)
    private Long id;

    /**
     * Name of the requested book.
     * This field cannot be null.
     */
    @Column(name = "book_name", nullable = false)
    private String bookName;

    /**
     * The user who requested the book.
     * Implements many-to-one relationship with the Users entity.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    private Users requestedBy;

    /**
     * Timestamp when the book was requested.
     * This field cannot be null and cannot be updated after creation.
     */
    @Column(name = "requested_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedAt;

    /**
     * Automatically sets the request timestamp when a new request is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.requestedAt = new Date();
    }
}
