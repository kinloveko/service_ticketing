package com.ticketing_project.Ticketing.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StatusController {

	@Autowired
	private StatusService statusService;

	@PostMapping("/status/add")
	public String addNewTicket(@ModelAttribute Status newStatus, RedirectAttributes redirectAttributes) {
		statusService.save(newStatus);

		redirectAttributes.addFlashAttribute("successMessage", "Ticket saved successfully!");
		return "redirect:/ticket_progress?ticketId=" + newStatus.getTicketID();
	}

}
