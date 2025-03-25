package com.ibizabroker.lms.entity;

import lombok.Data;

/**
 * Data transfer object for user registration requests.
 * This class represents the request payload when a new user attempts to register
 * in the library management system.
 *
 * @author codematrix
 * @version 1.0
 */
@Data
public class RegisterRequest {
    /**
     * The desired username for the new account.
     * Must be unique across all users.
     */
    private String username;

    /**
     * The full name of the user.
     */
    private String name;

    /**
     * The password for the new account.
     * Will be encrypted before storage.
     */
    private String password;

    /**
     * The physical address of the user.
     * This field is optional.
     */
    private String address;
}

