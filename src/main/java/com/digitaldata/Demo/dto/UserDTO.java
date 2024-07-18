package com.digitaldata.Demo.dto;



import com.digitaldata.Demo.enums.UserRole;

import lombok.Data;

@Data
public class UserDTO {


	private Long id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private UserRole userRole;
	
	
}
