package com.ticketing_project.Ticketing.Project;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String login(HttpSession session, HttpServletResponse response) {
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
		return "Login";
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
				session.setAttribute("address", i.getAddress());
				session.setAttribute("contactNumber", i.getContactNumber());
				session.setAttribute("profileImage", i.getProfileImage());
				session.setAttribute("status", i.getStatus());
				// Set response headers to disable caching
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
				response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
				response.setHeader("Expires", "0"); // Proxies.

				switch (i.getUserRole()) {
				case "client":
					return "dashboard";

				case "sales_team":
				case "support_team":
				case "billing_team":
				case "treasury_team":
				case "collection_team":
				case "sales_team_leader":
				case "support_team_leader":
				case "treasury_team_leader":
				case "billing_team_leader":
				case "collection_team_leader":
				case "super_admin":
					if (i.getStatus().equals("verified")) {
						m.addAttribute("users", users);
						return "admin.ark";
					} else if (i.getStatus().equals("pending")) {
						redirectAttributes.addFlashAttribute("errorLogin", "Account not yet verified!");
						return "notVerified";
					}
					break;
				}
			}
		}
		return "redirect:/Login";
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
		session.removeAttribute("address");
		session.removeAttribute("contactNumber");
		session.removeAttribute("profileImage");
		session.removeAttribute("status");
		
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/save")
	public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

		User existingUser = userService.findByUserEmail(user.getUser_email());

		if (existingUser == null) {
			userService.save(user);
			redirectAttributes.addFlashAttribute("successMessage", "Account registered successfully!");
		} else {
			redirectAttributes.addFlashAttribute("emailDuplicate", "Email already exists!");
		}

		return "redirect:/";

	}

	@GetMapping("/userDetails")
	@ResponseBody
	public String getFeedbacksByStatusId(@RequestParam int userId) {
		Optional<User> userDetails = userService.findUserById(userId);
		String email = userDetails.get().getUser_email();
		return email;
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

	@PutMapping("/user/update/profile")
	@ResponseBody
	public String updateProfile(@RequestParam int user_id, @ModelAttribute User updateUsers,
			@RequestParam MultipartFile img1, RedirectAttributes redirectAttributes,HttpSession session) {

		Optional<User> user = userService.findUserById(user_id);

		User updateUserData = user.get();
		//if img1 is null don't set this imgae to ex
		if(img1!=null)
		updateUserData.setProfileImage(img1.getOriginalFilename());
		
		updateUserData.setAddress(updateUsers.getAddress());
		updateUserData.setUser_password(updateUsers.getUser_password());
		updateUserData.setUser_email(updateUsers.getUser_email());
		updateUserData.setUser_name(updateUsers.getUser_name());
		updateUserData.setContactNumber(updateUsers.getContactNumber());
		updateUserData.setStatus(updateUsers.getStatus());
		updateUserData.setUserRole(updateUsers.getUserRole());
		userService.save(updateUserData);
		
		session.setAttribute("user_email", updateUserData.getUser_email());
		session.setAttribute("user_password", updateUserData.getUser_password());
		session.setAttribute("user_id", updateUserData.getUser_id());
		session.setAttribute("user_name", updateUserData.getUser_name());
		session.setAttribute("userRole", updateUserData.getUserRole());
		session.setAttribute("address", updateUserData.getAddress());
		session.setAttribute("contactNumber", updateUserData.getContactNumber());
		session.setAttribute("profileImage", updateUserData.getProfileImage());
		session.setAttribute("status", updateUserData.getStatus());
		
		if (img1 != null) {

			if (updateUserData != null) {
				try {
					// Save proof of payment file
					File saveFile = new ClassPathResource("static/img").getFile();
					Path proofPath = Paths
							.get(saveFile.getAbsolutePath() + File.separator + img1.getOriginalFilename());
					System.out.println(proofPath);
					System.out.println(img1.getOriginalFilename());
					Files.copy(img1.getInputStream(), proofPath, StandardCopyOption.REPLACE_EXISTING);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (updateUserData.getUserRole().equals("client")) {
			return "redirect:/dashboard";
		} else {
			return "redirect:/admin.ark";
		}
	}
	
	@PutMapping("/user/update-password/{userID}")
	@ResponseBody
	public String updatePassword(@PathVariable int userID,@RequestParam String user_password,HttpSession session,
			RedirectAttributes redirectAttributes) {
		Optional<User> existingUser = userService.findUserById(userID);

		existingUser.get().setUser_password(user_password);
		userService.save(existingUser.get());

		redirectAttributes.addFlashAttribute("updateMessage", "User updated successfully!");
		 session.setAttribute("user_password", user_password);
		return "redirect:/admin.ark";
	}

	
	

	@DeleteMapping("/user/delete/{userID}")
	@ResponseBody
	public void deleteTicket(@PathVariable int userID,HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setHeader("Expires", "0"); // Proxies.
			
		session.removeAttribute("user_email");
		session.removeAttribute("user_password");
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		session.removeAttribute("userRole");
		session.removeAttribute("address");
		session.removeAttribute("contactNumber");
		session.removeAttribute("profileImage");
		session.removeAttribute("status");
		
		session.invalidate();
		userService.deleteUser(userID);
		
	}

}
