package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.RegisterRequest;
import com.ibizabroker.lms.entity.JwtResponse;
import com.ibizabroker.lms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling user registration requests.
 * This controller provides endpoints for new user registration and account creation.
 *
 * @author codematrix
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class RegisterController {

    /**
     * Service responsible for user authentication and registration operations.
     */
    @Autowired
    private AuthService authService;

    /**
     * Registers a new user in the system.
     * Upon successful registration, returns a JWT token and user details.
     *
     * @param request The registration request containing user details
     * @return ResponseEntity containing either:
     *         - JwtResponse with token and user details (200 OK)
     *         - Error message if user already exists (400 Bad Request)
     *         - Generic error message for other failures (500 Internal Server Error)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            JwtResponse jwtResponse = authService.registerUser(request);
            return ResponseEntity.ok(jwtResponse);  // Send token + user details
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Return error if user exists
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while registering."); // Generic error
        }
    }
}


