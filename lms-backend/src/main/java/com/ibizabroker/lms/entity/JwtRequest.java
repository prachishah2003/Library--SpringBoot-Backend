package com.ibizabroker.lms.entity;

/**
 * Data transfer object for JWT authentication requests.
 * This class represents the request payload when a user attempts to authenticate
 * using their username and password.
 *
 * @author codematrix
 * @version 1.0
 */
public class JwtRequest {

    /**
     * The username of the user attempting to authenticate.
     */
    private String username;

    /**
     * The password of the user attempting to authenticate.
     */
    private String password;

    /**
     * Constructs a new JWT request with the given username and password.
     *
     * @param testUser The username for authentication
     * @param password The password for authentication
     */
    public JwtRequest(String testUser, String password) {
        this.username=testUser;
        this.password=password;
    }

    /**
     * Gets the username from the request.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username in the request.
     *
     * @param userName The username to set
     */
    public void setUserName(String userName) {
        this.username = userName;
    }

    /**
     * Gets the password from the request.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password in the request.
     *
     * @param userPassword The password to set
     */
    public void setUserPassword(String userPassword) {
        this.password = userPassword;
    }
}
