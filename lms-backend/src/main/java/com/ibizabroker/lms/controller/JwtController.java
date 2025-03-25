package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.JwtRequest;
import com.ibizabroker.lms.entity.JwtResponse;
import com.ibizabroker.lms.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling JWT (JSON Web Token) authentication requests.
 * This controller provides endpoints for user authentication and token generation.
 *
 * @author codematrix
 * @version 1.0
 */
@RestController
@CrossOrigin
//@RequestMapping("/")
public class JwtController {

    /**
     * Service responsible for JWT token generation and validation.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param jwtRequest The authentication request containing username and password
     * @return JwtResponse containing the generated token and user information
     * @throws Exception if authentication fails or token generation encounters an error
     */
    @PostMapping("/authenticate")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }
}