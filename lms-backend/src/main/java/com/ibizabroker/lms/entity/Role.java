package com.ibizabroker.lms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * Entity class representing a role in the library management system.
 * This class maps to the "role" table in the database and defines different
 * roles that can be assigned to users (e.g., ADMIN, USER).
 *
 * @author codematrix
 * @version 1.0
 */
@Data
@Entity
@Table(name = "role") // Lowercase for PostgreSQL compatibility
public class Role {

    /**
     * Unique identifier for the role.
     * Generated using a sequence generator for PostgreSQL compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * Name of the role (e.g., "ADMIN", "USER").
     * This field cannot be null and must be unique.
     */
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
}
