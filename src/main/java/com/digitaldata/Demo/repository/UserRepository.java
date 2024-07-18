package com.digitaldata.Demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitaldata.Demo.entities.User;
import com.digitaldata.Demo.enums.UserRole;



public interface UserRepository extends JpaRepository<User, Long>{

	User findFirstByEmail(String email);

	User findByUserRole(UserRole admin);
}
