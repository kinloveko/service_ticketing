package com.ticketing_project.Ticketing.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home() {
 	return "Login";
	}
	
	@PostMapping("/save")
	public String addUser(@ModelAttribute User user) {
		userService.save(user);
		
		return "redirect:/";		
	}
	
}
