package com.ibizabroker.lms.service;

import com.ibizabroker.lms.entity.Feedback;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.dao.FeedbackRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing user feedback in the library system.
 * Handles feedback submission, retrieval, and deletion operations.
 * Integrates with Spring Security for user authentication and authorization.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
public class FeedbackService {

    /**
     * Repository for performing database operations on feedback.
     */
    private final FeedbackRepository feedbackRepository;

    /**
     * Repository for accessing user information.
     */
    private final UsersRepository usersRepository;

    /**
     * Constructs a new FeedbackService with required repositories.
     *
     * @param feedbackRepository Repository for feedback operations
     * @param usersRepository Repository for user operations
     */
    public FeedbackService(FeedbackRepository feedbackRepository, UsersRepository usersRepository) {
        this.feedbackRepository = feedbackRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * Retrieves the username of the currently authenticated user.
     * Uses Spring Security's SecurityContextHolder to access authentication details.
     *
     * @return The username of the authenticated user, or null if not authenticated
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    /**
     * Submits new feedback from the currently authenticated user.
     * Associates the feedback with the user and saves it to the database.
     *
     * @param feedback The feedback to submit
     * @return The saved feedback with user association
     * @throws RuntimeException if user is not authenticated or not found
     */
    public Feedback submitFeedback(Feedback feedback) {
        String username = getCurrentUsername();
        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        feedback.setUser(user);
        return feedbackRepository.save(feedback);
    }

    /**
     * Retrieves all feedback from the system, ordered by creation date.
     *
     * @return List of all feedback entries, newest first
     */
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Deletes a specific feedback entry by its ID.
     *
     * @param feedbackId The ID of the feedback to delete
     * @throws RuntimeException if the feedback is not found
     */
    public void deleteFeedback(Long feedbackId) {
        Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
        if (feedback.isPresent()) {
            feedbackRepository.deleteById(feedbackId);
        } else {
            throw new RuntimeException("Feedback not found");
        }
    }
}
