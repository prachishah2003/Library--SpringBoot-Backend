package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for managing Role entities in the database.
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality.
 * Provides custom queries for role management and user authorization.
 *
 * @author codematrix
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    /**
     * Finds a role by its name.
     * Used for role lookup during user registration and authorization.
     *
     * @param roleName The name of the role to find
     * @return Optional containing the role if found, empty otherwise
     */
    Optional<Role> findByRoleName(String roleName);
}

