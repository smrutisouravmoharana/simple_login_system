package com.digitaldata.Demo.service;

import java.util.List;

import com.digitaldata.Demo.dto.SignupDTO;
import com.digitaldata.Demo.dto.UserDTO;

public interface UserService {

	UserDTO createUser(SignupDTO signupDTO);

	boolean hasUserWithEmail(String email);
	
	List<UserDTO> getAllUsers(); // Add this method

}
