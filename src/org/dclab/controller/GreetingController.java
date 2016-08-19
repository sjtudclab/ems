package org.dclab.controller;

import java.util.Map;

import org.dclab.model.ExamOperator;
import org.dclab.model.SupervisorOperator;
import org.dclab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class GreetingController {

/*	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();*/

	@Autowired
	private UserService userService;
	public void serUserService(UserService service){
		userService=service;
	}
	
	@RequestMapping("/greeting")
	public Object greeting(@RequestParam(value = "name") String name) {
		
/*		ExamOperator.load(1);
		SupervisorOperator.load();*/
		
		return userService.login(name);
	}
}
	
