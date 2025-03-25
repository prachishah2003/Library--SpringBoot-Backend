package com.ibizabroker.lms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity class representing user feedback in the library management system.
 * This class maps to the "feedback" table in the database and stores
 * user feedback along with ratings and timestamps.
 *
 * @author codematrix
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "feedback") // Lowercase for PostgreSQL compatibility
public class Feedback {

    /**
     * Unique identifier for the feedback record.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_seq")
    @SequenceGenerator(name = "feedback_seq", sequenceName = "feedback_seq", allocationSize = 1)
    private Long id;

    /**
     * The user who provided the feedback.
     * Implements many-to-one relationship with the Users entity.
     * This field is optional (nullable).
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Users user;

    /**
     * The text content of the feedback.
     * This field cannot be null.
     */
    @Column(name = "feedback_text", nullable = false)
    private String feedbackText;

    /**
     * Rating associated with the feedback.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private int rating;

    /**
     * Timestamp when the feedback was created.
     * This field cannot be null and cannot be updated after creation.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    /**
     * Automatically sets the creation timestamp when new feedback is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
