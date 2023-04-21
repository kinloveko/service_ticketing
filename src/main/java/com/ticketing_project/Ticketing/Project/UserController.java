package com.ticketing_project.Ticketing.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




@Controller
public class UserController {

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home() {
 	return "Login";
	}

	
	
	@PostMapping("/save")
	public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		userService.save(user);
		redirectAttributes.addFlashAttribute("successMessage", "Account registered successfully!");
		return "redirect:/";	
		
	}
	
 
	@PutMapping("/user/update/{userID}")
	@ResponseBody
	public void updateUser(@PathVariable final int userID,
			  @RequestParam(value = "selectedValue") String selectedValue,
			RedirectAttributes redirectAttributes) {
	    Optional<User> existingUser = userService.findUserById(userID);

	    existingUser.get().setStatus(selectedValue);
	    redirectAttributes.addFlashAttribute("updateMessage", "User updated successfully!");
	    userService.save(existingUser.get());
	  
	}
	
	
	@PostMapping("/login")
	public String login(HttpServletRequest request, HttpSession session,Model m) {
	    String user_email = request.getParameter("user_email");
	    String user_password = request.getParameter("user_password");
	    List<User> users = userService.getAllUsers();
	    List<User> accounts = new ArrayList<>();	
	    // Find the user with the matching email and password
	    for(User i:users) {
	    	 
	    	 
	        if(i.getUser_email().equals(user_email) && i.getUser_password().equals(user_password)) {
	        	       
	        	session.setAttribute("user_email", user_email);
	        	session.setAttribute("user_password", user_password);
	        	session.setAttribute("user_id", i.getUser_id());
	        	session.setAttribute("user_name", i.getUser_name());
	        	session.setAttribute("userRole", i.getUserRole());
	        	
	        	
	        	if(i.getUserRole().equals("client")) {
	        	      return "redirect:/dashboard"; // return the name of the dashboard page
	        	}
	        	else if (i.getUserRole().equals("sales_team")
	        			|| i.getUserRole().equals("support_team")
	        			|| i.getUserRole().equals("billing_team")
	        			|| i.getUserRole().equals("collection_team")
	        			|| i.getUserRole().equals("treasury_team")
	        			|| i.getUserRole().equals("super_admin")){
	        			
	        		   m.addAttribute("users", users);
	        		 return "redirect:/admin.ark"; // return the name of the dashboard page
	        	}
	        }
	    }
	    
	    return "login"; // return the name of the login page if no matching user is found
	}
	
	@GetMapping("/userDetails")
	@ResponseBody
	public String getFeedbacksByStatusId(@RequestParam int userId) {
	    Optional<User> userDetails = userService.findUserById(userId);
	    String email =userDetails.get().getUser_email();
	    return email;
	}
	
	
    //GET mapping method for /admin.ark
    @GetMapping("/admin.ark")
    public String adminPage(Model m,HttpSession session) {
    	 List<User> users = userService.getAllUsers();
        ticketService.populateTicketModel(m);
        m.addAttribute("users", users);
        return "admin.ark";
    }
    

    //GET mapping method for /admin.ark
    @GetMapping("/admin.register")
    public String adminRegistration(Model m) {
       
        return "admin.register";
    }
    
    
    @GetMapping("/getUserUpdate")
    public String userUpdate(Model m) {
    	m.addAttribute("users",userService.getAllUsers());
        return "admin.ark :: #account-table";
    }
    
    @GetMapping("/getUpdateTicket")
    public String adminPages(Model m) {
        ticketService.populateTicketModel(m);
        return "admin.ark :: #completed-table";
    }
    
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_email");
		session.removeAttribute("user_password");
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		session.removeAttribute("userRole");
		return "login";
	}
}
