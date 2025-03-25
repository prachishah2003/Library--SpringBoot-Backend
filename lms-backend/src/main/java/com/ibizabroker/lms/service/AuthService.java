package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dao.RoleRepository;
import com.ibizabroker.lms.entity.RegisterRequest;
import com.ibizabroker.lms.entity.JwtResponse;
import com.ibizabroker.lms.entity.Role;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Service class responsible for user authentication and registration operations.
 * Handles new user registration, role assignment, and initial JWT token generation.
 * This service ensures secure user creation with proper password encoding and role assignment.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
public class AuthService {

    /**
     * Repository for performing CRUD operations on user entities.
     */
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Repository for managing user roles and permissions.
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Password encoder for securely hashing user passwords.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Utility class for JWT token operations.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user in the system.
     * This method performs the following operations:
     * 1. Checks if the username is already taken
     * 2. Creates or retrieves the default user role
     * 3. Creates a new user with encoded password
     * 4. Assigns the default role and initial account balance
     * 5. Generates and returns a JWT token for immediate authentication
     *
     * @param request The registration request containing user details
     * @return JwtResponse containing the new user details and JWT token
     * @throws IllegalArgumentException if the username is already taken
     */
    public JwtResponse registerUser(RegisterRequest request) {
        // Check if the user already exists
        Optional<Users> existingUser = usersRepository.findByUsername(request.getUsername());

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists!");
        }

        // Ensure the "ROLE_USER" role exists
        Role userRole = roleRepository.findByRoleName("User")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("User");
                    return roleRepository.save(newRole);
                });

        // Create and save the new user
        Users newUser = new Users();
        newUser.setUsername(request.getUsername());
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encode the password
        newUser.setRole(Collections.singleton(userRole));
        newUser.setAccountBalance(500.0);
        newUser.setAddress(request.getAddress());
        usersRepository.save(newUser);

        // Generate JWT Token without re-authenticating
        UserDetails userDetails = new User(
                newUser.getUsername(), newUser.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority( "ROLE_" +userRole.getRoleName()))
        );

        String token = jwtUtil.generateToken(userDetails);

        return new JwtResponse(newUser, token);
    }
}
