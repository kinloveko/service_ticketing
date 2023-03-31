package com.ticketing_project.Ticketing.Project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Random;
@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/tickets/post-ticket")
	public void addNewTicket(@ModelAttribute Ticket newTicket) {
		ticketService.save(newTicket);
	}
	
	@PutMapping("/tickets/update-ticket/{ticketId}")
	@ResponseBody
	public void updateTicket(@PathVariable final int ticketId, @ModelAttribute Ticket updatedTicket) {
		ticketService.update(ticketId, updatedTicket);
	}
	
	// HTTP GET methods
	
	@GetMapping("/tickets/all")
	@ResponseBody
	public List<Ticket> getAllTickets(){
		return ticketService.getAllTickets();
	}
	
	@GetMapping("/tickets/all/filter")
	@ResponseBody
	public List<Ticket> getAllTicketsByStatus(@RequestParam final String status){
		return ticketService.getTicketsByStatus(status);
	}
	
	@GetMapping("/tickets/{ticketId}")
	public Ticket getTicketById(int ticketId) {
		return ticketService.getTicketById(ticketId);
	}
	
	@GetMapping("/tickets/assigned/{userId}")
	@ResponseBody
	public List<Ticket> getTicketsByUserId(@PathVariable final int userId){
		return ticketService.getTicketsByUserId(userId);
	}
	
	@GetMapping("/tickets/assigned/{userId}/filter")
	@ResponseBody
	public List<Ticket> getTicketsByUserIdAndStatus(@PathVariable int userId, 
			@RequestParam String status){
		return ticketService.getTicketsByStatusAndUserId(userId, status);
	}
}
