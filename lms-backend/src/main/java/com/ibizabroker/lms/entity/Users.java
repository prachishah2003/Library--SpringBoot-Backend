package com.ibizabroker.lms.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

/**
 * Entity class representing a user in the library management system.
 * This class maps to the "users" table in the database and contains user information
 * including authentication details, account balance, and role assignments.
 *
 * @author codematrix
 * @version 1.0
 */
@Data
@Entity
@Table(name = "users") // Lowercase table name for PostgreSQL compatibility
public class Users {

    /**
     * Unique identifier for the user.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Integer userId;

    /**
     * Unique username for the user account.
     * This field cannot be null and must be unique.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Full name of the user.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Encrypted password for user authentication.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Current balance in the user's account.
     * Default value is 500.
     * This field cannot be null.
     */
    @Column(nullable = false)
    private double accountBalance = 500;

    /**
     * User's physical address.
     * This field is optional.
     */
    @Column(nullable = true)
    private String address;

    /**
     * Set of roles assigned to the user.
     * Implements many-to-many relationship with the Role entity.
     * Uses eager fetching and cascade operations for better performance.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role;
}
