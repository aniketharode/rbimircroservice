package com.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.request.UsersRequest;
import com.users.service.UsersService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	Environment env;

	@Autowired
	private UsersService userservice;

	@GetMapping("/status/check")
	public String getUsers() {
		return "hello from user services from " + env.getProperty("local.server.port")+" secret token is"+env.getProperty("token.secret");
	}

	
	@PostMapping("/adduser")
	public ResponseEntity<Object> createUsers(@RequestBody UsersRequest request) {

		return userservice.addUsers(request);

	}

	
	@PostMapping("/getEcb")
	public String  getEcb() {

	  return "get ecb";

	}
	
	
	@GetMapping("/getuser/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable int id){
		
		UsersRequest req = userservice.getUserByUserId(Long.valueOf(id));
		
		return new ResponseEntity<Object>(req, HttpStatus.OK);
		
		
	}
	
	
	
	

}
