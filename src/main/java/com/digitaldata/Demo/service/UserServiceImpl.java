package com.digitaldata.Demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitaldata.Demo.controller.SignupController;
import com.digitaldata.Demo.dto.SignupDTO;
import com.digitaldata.Demo.dto.UserDTO;
import com.digitaldata.Demo.entities.User;
import com.digitaldata.Demo.enums.UserRole;
import com.digitaldata.Demo.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	
	
	
	@PostConstruct
	public void createAdminAccount() {
		User user = userRepository.findByUserRole(UserRole.ADMIN);
		if(user == null) {
			User user2 = new User();
			user2.setUserRole(UserRole.ADMIN);
			user2.setEmail("admin@test.com");
			user2.setName("admin");
			user2.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user2);
		}
	}
	
	@Override
	public UserDTO createUser(SignupDTO signupDTO) {
	    User user = new User();
	    user.setName(signupDTO.getName());
	    user.setEmail(signupDTO.getEmail());
	    user.setUserRole(UserRole.USER);
	    
	    // Encrypt password with bcrypt
	    String encryptedPassword = new BCryptPasswordEncoder().encode(signupDTO.getPassword());
	    user.setPassword(encryptedPassword);
	    
	    // Save user to repository
	    User createdUser = userRepository.save(user);

	    // Create UserDTO with necessary details
	    UserDTO userDTO = new UserDTO();
	    userDTO.setId(createdUser.getId());
	    userDTO.setName(createdUser.getName());
	    userDTO.setEmail(createdUser.getEmail());
	    userDTO.setUserRole(createdUser.getUserRole());
	    userDTO.setPassword(encryptedPassword); // Include encrypted password in response

	    // Optionally, log encrypted password for internal use
	    logger.info("User created with email: {} and bcrypt password: {}", signupDTO.getEmail(), encryptedPassword);

	    return userDTO;
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		return userRepository.findFirstByEmail(email) != null;
	}
	
	 @Override
	    public List<UserDTO> getAllUsers() {
	        List<User> users = userRepository.findAll();
	        return users.stream().map(user -> {
	            UserDTO userDTO = new UserDTO();
	            userDTO.setId(user.getId());
	            userDTO.setName(user.getName());
	            userDTO.setEmail(user.getEmail());
	            userDTO.setUserRole(user.getUserRole());
	            userDTO.setPassword(user.getPassword());
	            return userDTO;
	        }).collect(Collectors.toList());
	    }

	
}
