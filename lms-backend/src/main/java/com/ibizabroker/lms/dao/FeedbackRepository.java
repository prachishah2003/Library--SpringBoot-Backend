package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Feedback entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for retrieving and managing user feedback.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    /**
     * Retrieves all feedback entries ordered by creation date in descending order.
     * This ensures that the most recent feedback appears first in the list.
     *
     * @return List of feedback entries sorted by creation date (newest first)
     */
    List<Feedback> findAllByOrderByCreatedAtDesc();

// Fetch feedbacks in descending order
}
