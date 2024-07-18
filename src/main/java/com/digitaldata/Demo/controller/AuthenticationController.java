package com.digitaldata.Demo.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.digitaldata.Demo.configuration.AuthenticationRequest;
import com.digitaldata.Demo.configuration.AuthenticationResponse;
import com.digitaldata.Demo.entities.User;
import com.digitaldata.Demo.repository.UserRepository;
import com.digitaldata.Demo.utils.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

	 private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private UserDetailsService userDetailsService;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private JwtUtil jwtUtil;

	    @PostMapping("/authenticate")
	    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
	            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws BadCredentialsException,
	            DisabledException, UsernameNotFoundException, IOException, ServletException {
	        try {
	            logger.info("Authenticating user: {}", authenticationRequest.getUsername());
	            authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
	        } catch (BadCredentialsException e) {
	            logger.error("Authentication failed for user: {}", authenticationRequest.getUsername(), e);
	            throw new BadCredentialsException("Incorrect Username or Password", e);
	        } catch (DisabledException disabledException) {
	            logger.warn("User is not activated: {}", authenticationRequest.getUsername());
	            httpServletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "User is not activated");
	            return null;
	        }

	        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());
	        final String jwt = jwtUtil.generateToken(user);
	        logger.info("Generated JWT token for user: {}", authenticationRequest.getUsername());
	        return new AuthenticationResponse(jwt);
	    }
}
