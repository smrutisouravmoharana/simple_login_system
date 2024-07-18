package com.digitaldata.Demo.configuration;

import lombok.Data;

@Data
public class AuthenticationRequest {

	private String username;
	
	private String password;
}
