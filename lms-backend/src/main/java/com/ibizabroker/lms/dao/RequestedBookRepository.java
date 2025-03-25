package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.RequestedBook;
import com.ibizabroker.lms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for managing RequestedBook entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for managing book requests and their relationships with users.
 *
 * @author codematrix
 * @version 1.0
 */
public interface RequestedBookRepository extends JpaRepository<RequestedBook, Long> {

    /**
     * Retrieves all book requests with their associated user information.
     * Uses JOIN FETCH to eagerly load the requestedBy relationship to avoid N+1 queries.
     *
     * @return List of RequestedBook entities with associated user details
     */
    @Query("SELECT r FROM RequestedBook r JOIN FETCH r.requestedBy")
    List<RequestedBook> findAllWithUsers();

    /**
     * Deletes all book requests made by a specific user.
     * This is a modifying query that requires a transaction.
     *
     * @param user The user whose requests should be deleted
     */
    @Modifying
    @Transactional
    void deleteByRequestedBy(Users user);

    /**
     * Deletes all book requests made by a user with the specified ID.
     * This is a modifying query that requires a transaction.
     * Uses a JPQL query to perform the deletion based on user ID.
     *
     * @param userId The ID of the user whose requests should be deleted
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RequestedBook r WHERE r.requestedBy.id = :userId")
    void deleteByRequestedById(Integer userId);
}
