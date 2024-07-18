package com.digitaldata.Demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitaldata.Demo.dto.SignupDTO;
import com.digitaldata.Demo.dto.UserDTO;
import com.digitaldata.Demo.service.UserService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/api/v1")
public class SignupController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
        logger.info("Received sign-up request for email: {}", signupDTO.getEmail());

        if (userService.hasUserWithEmail(signupDTO.getEmail())) {
            logger.warn("User already exists with email: {}", signupDTO.getEmail());
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        UserDTO createUser = userService.createUser(signupDTO);
        if (createUser == null) {
            logger.error("User not created for email: {}", signupDTO.getEmail());
            return new ResponseEntity<>("User not created, please try again later!", HttpStatus.BAD_REQUEST);
        }

        logger.info("User created successfully for email: {}", signupDTO.getEmail());
        // Return response with bcrypt hashed password and success message
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully registered user with email: " + signupDTO.getEmail() + ". User details: " + createUser);
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
