package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for user management and authentication.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    
    /**
     * Finds a user by their username.
     * Used primarily for authentication and user lookup operations.
     *
     * @param username The username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<Users> findByUsername(String username);
}
