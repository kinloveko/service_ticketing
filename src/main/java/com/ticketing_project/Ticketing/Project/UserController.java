package com.ticketing_project.Ticketing.Project;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;




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

<<<<<<< HEAD
	
	
=======
>>>>>>> 977990951b9cc323b927cd061510243ef7ad865a
	@PostMapping("/save")
	public String addUser(@ModelAttribute User user) {
		userService.save(user);
		
		return "redirect:/";	
		
	}
	
	@PostMapping("/login")
	public String login(HttpServletRequest request, HttpSession session) {
	    String user_email = request.getParameter("user_email");
	    String user_password = request.getParameter("user_password");
	    List<User> users = userService.getAllUsers();
	    // Find the user with the matching email and password
	    for(User i:users) {
	    	 System.out.print(i.getUser_email()+" "+i.getUser_password());
	    	 
	        if(i.getUser_email().equals(user_email) && i.getUser_password().equals(user_password)) {
	        		       
	        	session.setAttribute("user_email", user_email);
	        	session.setAttribute("user_password", user_password);
	        	session.setAttribute("user_id", i.getUser_id());
	        	session.setAttribute("user_name", i.getUser_name());
	        	session.setAttribute("user_role", i.getUser_role());
	        
	        	if(i.getUser_role().equals("client")) {
	        	      return "redirect:/dashboard"; // return the name of the dashboard page
	        	}
	        	else if (i.getUser_role().equals("sales_team")){
	        		 return "redirect:/admin.ark"; // return the name of the dashboard page
	        	}
	        	
	      
	        }
	    }
	    return "login"; // return the name of the login page if no matching user is found
	}
	

	
	@GetMapping("/admin.ark")
public String adminPage(Model m) {
		
    List<Ticket> list = ticketService.getAllTickets();

    
    int pendingCount = 0;
    int completedCount = 0;
    int ongoingCount = 0;
    
	List<Ticket> pendingTickets = new ArrayList<>();
	List<Ticket> ongoingTickets = new ArrayList<>();
	List<Ticket> completedTickets  = new ArrayList<>();
	
	
	for(Ticket t : list) {
        if(t.getStatus().equals("pending")) {
            pendingTickets.add(t);
            pendingCount++;
        }
        else if(t.getStatus().equals("on_going")) {
        	   ongoingTickets.add(t);
               ongoingCount++;
        }
        else if(t.getStatus().equals("completed")) {
        	 completedTickets.add(t);
        	 completedCount++;
        }
}

	m.addAttribute("pending_tickets", pendingTickets);
    m.addAttribute("pending_ticket_count", pendingCount);
    
    m.addAttribute("completed_tickets", completedTickets);
    m.addAttribute("completed_ticket_count", completedCount);
    
    m.addAttribute("ongoing_tickets",  ongoingTickets);
    m.addAttribute("ongoing_ticket_count", ongoingCount);
    
    return "admin.ark";

}
	
	
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_email");
		session.removeAttribute("user_password");
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		session.removeAttribute("user_role");
		return "login";
	}
}
