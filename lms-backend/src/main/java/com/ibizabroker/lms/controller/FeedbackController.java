package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.Feedback;
import com.ibizabroker.lms.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling user feedback operations in the library management system.
 * Provides endpoints for submitting, retrieving, and managing user feedback.
 * Some endpoints require admin privileges.
 *
 * @author codematrix
 * @version 1.0
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    /**
     * Service responsible for handling feedback operations.
     */
    private final FeedbackService feedbackService;

    /**
     * Constructs a new FeedbackController with the specified service.
     *
     * @param feedbackService The service to handle feedback operations
     */
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * Submits new feedback from a user.
     *
     * @param feedback The feedback details to submit
     * @return ResponseEntity containing either:
     *         - The saved feedback (200 OK)
     *         - Error message if submission fails (400 Bad Request)
     */
    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody Feedback feedback) {
        try {
            Feedback savedFeedback = feedbackService.submitFeedback(feedback);
            return ResponseEntity.ok(savedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retrieves all feedback submitted to the system.
     * Requires admin privileges.
     *
     * @return ResponseEntity containing the list of all feedback
     */
    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    /**
     * Deletes a specific feedback entry.
     * Requires admin privileges.
     *
     * @param id The ID of the feedback to delete
     * @return ResponseEntity containing either:
     *         - Success message (200 OK)
     *         - Error message if deletion fails (400 Bad Request)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok("Feedback deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
