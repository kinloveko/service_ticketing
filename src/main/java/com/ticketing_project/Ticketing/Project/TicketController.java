package com.ticketing_project.Ticketing.Project;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/tickets/post-ticket")
	public String addNewTicket(@ModelAttribute Ticket newTicket) {
		ticketService.save(newTicket);
		return "redirect:/dashboard";
	}
	
	/*
	 * @GetMapping("/tickets/all")
	 * 
	 * @ResponseBody public List<Ticket> getAllTickets(){ return
	 * ticketService.getAllTickets(); }
	 */
	
	@GetMapping("/dashboard")
	public ModelAndView getAllTicket() {
		List<Ticket>list=ticketService.getAllTickets();
//		ModelAndView m = new ModelAndView();
//		m.setViewName("dashboard");
//		m.addObject("ticket",list);
		return new ModelAndView("dashboard","Ticket",list);
	}
	
	@GetMapping("/tickets/all/filter")
	@ResponseBody
	public List<Ticket> getAllTicketsByStatus(@RequestParam final String status){
		return ticketService.getTicketsByStatus(status);
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
