package com.example.jwt.controller;

import com.example.jwt.dto.JwtRequest;
import com.example.jwt.dto.JwtResponse;
import com.example.jwt.exception.ExceptionBroker;
import com.example.jwt.model.Users;
import com.example.jwt.service.UserServiceToRepo;
import com.example.jwt.utility.GoogleOAuthUtility;
import com.example.jwt.utility.JWTUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	
	private JWTUtility jwtUtility;
	private UserServiceToRepo userServicetRepo;

	@Autowired
	public HomeController(JWTUtility jwtUtility, UserServiceToRepo userServicetRepo) {
		this.jwtUtility = jwtUtility;
		this.userServicetRepo = userServicetRepo;
	}

	@GetMapping("/")
	public String basicControllerString() {
		return "Hello World";
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws ExceptionBroker {

		GoogleOAuthUtility gAuth = new GoogleOAuthUtility();
		Users user = gAuth.verifyUserFromIdToken(jwtRequest.getIdToken());

		userServicetRepo.insertUserService(user, jwtRequest.getUserName());

		final String token = jwtUtility.generateToken(user);
		return new JwtResponse(token);
	}

}
