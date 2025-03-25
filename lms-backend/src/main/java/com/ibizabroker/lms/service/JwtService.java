package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.entity.JwtRequest;
import com.ibizabroker.lms.entity.JwtResponse;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class responsible for JWT (JSON Web Token) operations and user authentication.
 * Implements Spring Security's UserDetailsService for user authentication and authorization.
 * This service handles token generation, user authentication, and user details loading.
 *
 * @author codematrix
 * @version 1.0
 */
@Service
public class JwtService implements UserDetailsService {

    /**
     * Utility class for JWT token operations like generation and validation.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Repository for accessing user data from the database.
     */
    @Autowired
    private UsersRepository userDao;

    /**
     * Spring Security's authentication manager for handling user authentication.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Creates a JWT token for a user after successful authentication.
     * 
     * @param jwtRequest The request containing user credentials (username and password)
     * @return JwtResponse containing the generated token and user details
     * @throws Exception if authentication fails or user is disabled
     */
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        authenticate(username, password);

        UserDetails userDetails = loadUserByUsername(username);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        Users user = userDao.findByUsername(username).get();
        return new JwtResponse(user, newGeneratedToken);
    }

    /**
     * Loads user details by username for Spring Security authentication.
     * Implements UserDetailsService interface method.
     *
     * @param username The username to load details for
     * @return UserDetails object containing user's security information
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userDao.findByUsername(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    /**
     * Gets the authorities/roles granted to a user.
     * Converts user roles to Spring Security SimpleGrantedAuthority objects.
     *
     * @param user The user whose authorities are to be retrieved
     * @return Set of SimpleGrantedAuthority objects representing user's roles
     */
    private Set getAuthority(Users user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    /**
     * Authenticates a user with the provided credentials.
     * Uses Spring Security's AuthenticationManager for authentication.
     *
     * @param userName The username for authentication
     * @param userPassword The password for authentication
     * @throws Exception if user is disabled or credentials are invalid
     */
    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}