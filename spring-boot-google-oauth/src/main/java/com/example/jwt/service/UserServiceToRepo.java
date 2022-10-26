package com.example.jwt.service;

import com.example.jwt.model.Users;
import com.example.jwt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceToRepo {

	private UserRepository userRepository;

	@Autowired
	public UserServiceToRepo(UserRepository userRepositoryImpl) {
		this.userRepository = userRepositoryImpl;
	}

	public void insertUserService(Users user, String userName) {

		Boolean userExists = userRepository.existsById(user.getSub());
		if (!userExists) {
			user.setName(userName);
			userRepository.save(user);
		}
	}

}
