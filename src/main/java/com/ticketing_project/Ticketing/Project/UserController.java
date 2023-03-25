package com.ticketing_project.Ticketing.Project;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

	@GetMapping("/dashboard")
	public String dashboard() {
 	return "dashboard";
	}
	
	
	@PostMapping("/save")
	public String addUser(@ModelAttribute User user) {
		userService.save(user);
		
		return "redirect:/";	
		
	}
	
	@PostMapping("/login")
	public String login(HttpServletRequest request) {
	    String user_email = request.getParameter("user_email");
	    String user_password = request.getParameter("user_password");

	    List<User> users = userService.getAllUsers();
	    // Find the user with the matching email and password
	    for(User i:users) {
	    	 System.out.print(i.getUser_email()+" "+i.getUser_password());
	    	 
	        if(i.getUser_email().equals(user_email) && i.getUser_password().equals(user_password)) {
	            return "redirect:/dashboard"; // return the name of the dashboard page
	        }
	    }
	    return "login"; // return the name of the login page if no matching user is found
	}
}
