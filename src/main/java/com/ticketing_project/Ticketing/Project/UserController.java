package com.ticketing_project.Ticketing.Project;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	public String login(HttpSession session,HttpServletResponse response) {
	    // check if user role is not null in session storage
	    if (session.getAttribute("userRole") != null) {
	        String role = (String) session.getAttribute("userRole");
	        if (role.equals("client")) {
	            return "redirect:/dashboard";
	        } else {
	            return "redirect:/admin.ark";
	        }
	    }
		// Set response headers to disable caching
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
	    return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public String login(HttpServletRequest request, HttpSession session, Model m, RedirectAttributes redirectAttributes,
			HttpServletResponse response) {

		String user_email = request.getParameter("user_email");
		String user_password = request.getParameter("user_password");
		List<User> users = userService.getAllUsers();
		// Find the user with the matching email and password
		for (User i : users) {
			if (i.getUser_email().equals(user_email) && i.getUser_password().equals(user_password)) {
				session.setAttribute("user_email", user_email);
				session.setAttribute("user_password", user_password);
				session.setAttribute("user_id", i.getUser_id());
				session.setAttribute("user_name", i.getUser_name());
				session.setAttribute("userRole", i.getUserRole());
				
				// Set response headers to disable caching
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
				response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
				response.setHeader("Expires", "0"); // Proxies.
				
				if (i.getUserRole().equals("client")) {
					return "dashboard"; // return the name of the dashboard page
				} else if (i.getUserRole().equals("sales_team_leader") || i.getUserRole().equals("sales_team")
						|| i.getUserRole().equals("support_team") || i.getUserRole().equals("support_team_leader")
						|| i.getUserRole().equals("billing_team") || i.getUserRole().equals("collection_team")
						|| i.getUserRole().equals("collection_team_leader")
						|| i.getUserRole().equals("treasury_team_leader")
						|| i.getUserRole().equals("billing_team_leader")
						|| i.getUserRole().equals("collection_team_leader")
						|| i.getUserRole().equals("collection_team_leader") || i.getUserRole().equals("treasury_team")
						|| i.getUserRole().equals("super_admin")) {

					if (i.getStatus().equals("verified")) {
						m.addAttribute("users", users);
						return "admin.ark";
					} else if (i.getStatus().equals("pending")) {
						redirectAttributes.addFlashAttribute("errorLogin", "Account not yet verified!");
						return "notVerified";
					}
				}
			}
		}
		return "redirect:/Login";
	}

	@GetMapping("/userDetails")
	@ResponseBody
	public String getFeedbacksByStatusId(@RequestParam int userId) {
		Optional<User> userDetails = userService.findUserById(userId);
		String email = userDetails.get().getUser_email();
		return email;
	}

	// GET mapping method for /admin.ark
	@GetMapping("/admin.ark")
	public String adminPage(Model m, HttpSession session, HttpServletResponse response) {

		List<User> users = userService.getAllUsers();
		ticketService.populateTicketModel(m);
		m.addAttribute("users", users);
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
		return "admin.ark";
	}

	// GET mapping method for /admin.ark
	@GetMapping("/admin.register")
	public String adminRegistration(Model m) {

		return "admin.register";
	}

	@GetMapping("/getUserUpdate")
	public String userUpdate(Model m) {
		m.addAttribute("users", userService.getAllUsers());
		return "admin.ark :: #account-table";
	}

	@GetMapping("/getUpdateTicket")
	public String getUpdateTicket(@RequestParam("tableId") String tableId, Model m) {
		ticketService.populateTicketModel(m);
		return "admin.ark :: #" + tableId;
	}

	@GetMapping("/updateClientTicket")
	public String getClientTicket(@RequestParam("tableId") String tableId, Model m, HttpSession session) {
		if (session != null) {
			int user_id = (int) session.getAttribute("user_id");
			ticketService.clientOwnTicket(m, user_id);
			return "dashboard :: #" + tableId;
		}
		return "dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.

		session.removeAttribute("user_email");
		session.removeAttribute("user_password");
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		session.removeAttribute("userRole");

		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/save")
	public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		userService.save(user);
		redirectAttributes.addFlashAttribute("successMessage", "Account registered successfully!");
		return "redirect:/";

	}

	@PutMapping("/user/update/{userID}")
	@ResponseBody
	public String updateUser(@PathVariable int userID, @RequestParam String selectedValue,
			RedirectAttributes redirectAttributes) {
		Optional<User> existingUser = userService.findUserById(userID);

		existingUser.get().setStatus(selectedValue);
		userService.save(existingUser.get());

		redirectAttributes.addFlashAttribute("updateMessage", "User updated successfully!");

		return "redirect:/users";
	}
}
