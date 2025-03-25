package com.ibizabroker.lms.entity;

/**
 * Data transfer object for JWT authentication responses.
 * This class represents the response payload when a user successfully authenticates,
 * containing the authenticated user's information and the generated JWT token.
 *
 * @author ibizabroker
 * @version 1.0
 */
public class JwtResponse {

    /**
     * The authenticated user's information.
     */
    private Users user;

    /**
     * The JWT token generated for the authenticated user.
     */
    private String jwtToken;

    /**
     * Constructs a new JWT response with the given user and token.
     *
     * @param user The authenticated user
     * @param jwtToken The generated JWT token
     */
    public JwtResponse(Users user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }

    /**
     * Gets the authenticated user from the response.
     *
     * @return The authenticated user
     */
    public Users getUser() {
        return user;
    }

    /**
     * Sets the authenticated user in the response.
     *
     * @param user The user to set
     */
    public void setUser(Users user) {
        this.user = user;
    }

    /**
     * Gets the JWT token from the response.
     *
     * @return The JWT token
     */
    public String getJwtToken() {
        return jwtToken;
    }

    /**
     * Sets the JWT token in the response.
     *
     * @param jwtToken The token to set
     */
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
