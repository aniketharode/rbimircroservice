package com.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.model.UsersModel;

public interface UsersRepo extends JpaRepository<UsersModel, Long> {
	
	public UsersModel findByUsername(String username);

}
