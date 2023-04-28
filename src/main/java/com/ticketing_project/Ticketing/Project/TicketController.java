package com.ticketing_project.Ticketing.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bouncycastle.mime.encoding.Base64OutputStream;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.commons.utils.Base64;
import com.itextpdf.commons.utils.Base64.OutputStream;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;

@Controller
public class TicketController {

	@Autowired
	private StatusService statusService;

	@Autowired
	private TicketService ticketService;
	// ticketController

	@GetMapping("/send-aging-ticket-reminders")
	@ResponseBody
	public void sendAgingTicketReminders() throws MessagingException {
		// Call the service method to send email reminders for aging tickets
		ticketService.sendAgingTicketReminders();

	}

	@PostMapping("/tickets/post-ticket")
	public String addNewTicket(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes,
			HttpSession session) {
		ticketService.save(newTicket);
		// Construct email message with data from new ticket
		if (session != null) {
			String userEmail = (String) session.getAttribute("user_email");

			String emailBody = "Dear Sales Team,\n\n" + "A new ticket has been created with the following details:\n\n"
					+ "Ticket ID: " + newTicket.getTicket_id() + "\n" + "Client Name: " + newTicket.getUser_name()
					+ "\n" + "Email Address: " + userEmail + "\n" + "Title: " + newTicket.getTitle() + "\n"
					+ "Description: " + newTicket.getDescription() + "\n\n"
					+ "Please take a moment to review the ticket and assign it to the appropriate team member. Let me know if you have any questions or concerns.\n\n"
					+ "Best regards, \n" + "Ark Alliance Company";
			String emailSubject = "New Ticket Created: Ticket ID #" + newTicket.getTicket_id();

			// Send email to sales team
			List<String> salesTeamEmails = ticketService.getEmailsOfSalesTeam();
			for (String email : salesTeamEmails) {
				ticketService.sendEmail(email, emailBody, emailSubject);
			}

			redirectAttributes.addFlashAttribute("successMessage", "Ticket saved successfully!");

		}

		return "redirect:/dashboard";
	}

	@PostMapping("/update-ticket")
	public String updateDescriptionTicket(@ModelAttribute Ticket updateTicket, RedirectAttributes redirectAttributes) {
		ticketService.save(updateTicket);
		redirectAttributes.addFlashAttribute("successMessage", "Ticket saved successfully!");
		return "redirect:/dashboard";
	}

	@GetMapping("/getTicket")
	@ResponseBody
	public String getTicket(@RequestParam int ticketID) {
		Ticket ticket = ticketService.getTicketById(ticketID);
		return ticket.getProgress();
	}

	// HTTP POST method
	@PostMapping("/tickets/update-ticket-ongoing")
	public String updateOngoing(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes) {
		ticketService.save(newTicket);

		redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");

		return "redirect:/admin.ark";

	}

	@PutMapping("/tickets/update-tickets-client/{ticketID}")
	@ResponseBody
	public String conforme_client_update(@PathVariable int ticketID, RedirectAttributes redirectAttributes,
			@RequestParam MultipartFile img1, @RequestParam MultipartFile img2) {
		Ticket newTicket = ticketService.getTicketById(ticketID);

		newTicket.setClient_payment_proof(img1.getOriginalFilename());
		newTicket.setClient_signature(img2.getOriginalFilename());

		Ticket savedTicket = ticketService.save(newTicket);

		if (savedTicket != null) {
			try {
				// Save proof of payment file
				File saveFile = new ClassPathResource("static/img").getFile();
				Path proofPath = Paths.get(saveFile.getAbsolutePath() + File.separator + img1.getOriginalFilename());
				System.out.println(proofPath);
				System.out.println(img1.getOriginalFilename());
				Files.copy(img1.getInputStream(), proofPath, StandardCopyOption.REPLACE_EXISTING);

				// Save client signature file
				Path signaturePath = Paths
						.get(saveFile.getAbsolutePath() + File.separator + img2.getOriginalFilename());
				System.out.println(signaturePath);
				System.out.println(img2.getOriginalFilename());
				Files.copy(img2.getInputStream(), signaturePath, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		redirectAttributes.addFlashAttribute("successMessage", "Conforme slip saved successfully!");
		return "redirect:/dashboard";
	}

	@PutMapping("/tickets/update-ticket/{ticketId}")
	@ResponseBody
	public void updateTicket(@PathVariable final int ticketId, @RequestParam(value = "progress") String progress,
			@RequestParam(value = "status") String status, RedirectAttributes redirectAttributes) {
		// Find the ticket to update using the ticketId path variable
		Ticket ticketToUpdate = ticketService.getTicketById(ticketId);

		ticketToUpdate.setStatus(status);
		ticketToUpdate.setProgress(progress);

		// Save the updated ticket
		ticketService.update(ticketId, ticketToUpdate);

	}

	@PutMapping("/tickets/update_ticket")
	@ResponseBody
	public void conformeTicket(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes,
			@RequestParam MultipartFile img) {

		newTicket.setSalesSignature(img.getOriginalFilename());

		Ticket uploadImg = ticketService.save(newTicket);

		if (uploadImg != null) {
			try {

				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
				System.out.println(path);
				System.out.println(img.getOriginalFilename());
				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		redirectAttributes.addFlashAttribute("conformeSlip", "Conforme slip generated successfully!");

	}

	// HTTP DELETE method

	@DeleteMapping("/tickets/delete/{ticketId}")
	@ResponseBody
	public void deleteTicket(@PathVariable final int ticketId) {
		ticketService.delete(ticketId);
	}

	@GetMapping("/tickets/{ticketId}")
	public String displayTicketDetailsById(@PathVariable("ticketId") int ticketId, HttpSession session, Model m) {
		
		Ticket ticket = ticketService.getTicketById(ticketId);
		List<Status> findStatus = statusService.getAllStatus();

		List<Status> filtered_status = new ArrayList<>();
		for (Status s : findStatus) {
			if (s.getTicketID() == ticketId) {
				filtered_status.add(s);
			}
		}
		if (session != null) {

			String userRole = (String) session.getAttribute("user_role");

			session.setAttribute("role", userRole);
			m.addAttribute("ticket", ticket);
			session.setAttribute("ticketId", ticketId);
			m.addAttribute("findStatus", filtered_status);
			System.out.println("Status updates for ticket " + ticketId + ": " + filtered_status);
			System.out.println("Role:" + userRole);
		}

		return "redirect:/ticket_progress?ticketId=" + ticketId;
	}

	@GetMapping("/ticket_progress")
	public String adminPage(Model m, @RequestParam("ticketId") int ticketId, HttpSession session) {
		if(session!=null) {

			Ticket ticket = ticketService.getTicketById(ticketId);
			List<Status> findStatus = statusService.getAllStatus();

			List<Status> filtered_status = new ArrayList<>();
			for (Status s : findStatus) {
				if (s.getTicketID() == ticketId) {
					filtered_status.add(s);
				}
			}

			Collections.sort(filtered_status, new Comparator<Status>() {
				@Override
				public int compare(Status s1, Status s2) {
					return s2.getStatus_id() - s1.getStatus_id();
				}
			});

			String userRole = (String) session.getAttribute("user_role");
			session.setAttribute("role", userRole);
			session.setAttribute("ticketId", ticketId);
			m.addAttribute("ticket", ticket);
			m.addAttribute("findStatus", filtered_status);
			System.out.println("Status updates for ticket " + ticketId + ": " + filtered_status);
			return "ticket_progress";
		}
		return "ticket_progress";
		
	}

	@GetMapping("/dashboard")
	public String getTicketsByUserId(HttpSession session, Model m ,HttpServletResponse response) {

		if(session.getAttribute("user_id")!=null) {
		int user_id = (int) session.getAttribute("user_id");

		ticketService.clientOwnTicket(m, user_id);
		  // Set response headers to disable caching
	    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	    response.setHeader("Expires", "0"); // Proxies.
	    
		}	

		
		return "dashboard";
	}

	@GetMapping("/tickets/csv")
	public String generateCsvFileByCreatedOn(@RequestParam String filter, HttpServletResponse response)
			throws IOException, java.io.IOException {
		// Generate CSV data
		if(response!=null)
		ticketService.generateCsvFileByCreatedOn(filter, response);
		return "admin.ark";
	}

	@GetMapping("/tickets/csv/assignee")
	public String generateCsvFileByAssignee(@RequestParam String asignee, HttpServletResponse response)
			throws IOException, java.io.IOException {
		// Generate CSV data
		if(response!=null)
		ticketService.generateCsvFileByAssignee(asignee, response);
		return "admin.ark";
	}

	@GetMapping("/tickets/csv/aging")
	public String generateCsvFileByAging(@RequestParam String aging, HttpServletResponse response)
			throws IOException, java.io.IOException {
		// Generate CSV data
		if(response!=null)
		ticketService.generateCsvFileByAging(aging, response);
		return "admin.ark";
	}
}