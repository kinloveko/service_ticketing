package com.ticketing_project.Ticketing.Project;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TicketController {
	
	/*
	 * @GetMapping("/tickets/assigned/{userId}/filter")
	 * 
	 * @ResponseBody public List<Ticket> getTicketsByUserIdAndStatus(@PathVariable
	 * int userId,
	 * 
	 * @RequestParam String status){ return
	 * ticketService.getTicketsByStatusAndUserId(userId, status); }
	 */
	
	/*
	 * @GetMapping("/tickets/all/filter")
	 * 
	 * @ResponseBody public List<Ticket> getAllTicketsByStatus(@RequestParam final
	 * String status){ return ticketService.getTicketsByStatus(status); }
	 */
	
	
	
	@Autowired
	private TicketService ticketService;
	
	//HTTP POST method
	@PostMapping("/tickets/post-ticket")
	public String addNewTicket(@ModelAttribute Ticket newTicket) {
		ticketService.save(newTicket);
		return "redirect:/dashboard";
		}
	
	@PutMapping("/tickets/update-ticket/{ticketId}")
	@ResponseBody
	public void updateTicket(@PathVariable final int ticketId, @ModelAttribute Ticket updatedTicket) {
		ticketService.update(ticketId, updatedTicket);
	}
	
	//HTTP DELETE method
	@DeleteMapping("/tickets/delete/{ticketId}")
	@ResponseBody
	public void deleteTicket(@PathVariable final int ticketId) {
		ticketService.delete(ticketId);
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
	
	

    @GetMapping("/dashboard")
    public ModelAndView getTicketsByUserId(HttpSession session) {
        int user_id = (int) session.getAttribute("user_id");
        List<Ticket>list=ticketService.getTicketsByUserId(user_id);
        
        List<Ticket> pendingTickets = new ArrayList<>();
        int countPending = 0;
        List<Ticket> ongoingTickets = new ArrayList<>();
        int goingPending = 0;
        List<Ticket> completedTickets = new ArrayList<>();
        int completedPending = 0;
        
        for(Ticket i:list) {
        	if(i.getStatus().equals("pending")) {
        		countPending++;
        		pendingTickets.add(i);
        	}
        	if(i.getStatus().equals("ongoing")) {
        		goingPending++;
        		ongoingTickets.add(i);
        	}
        }
        
        ModelAndView mav = new ModelAndView("dashboard");
        mav.addObject("pending_tickets", pendingTickets);
        mav.addObject("pending_count",countPending);
        mav.addObject("ongoing_tickets", ongoingTickets);
        mav.addObject("ongoing_count",goingPending);
        mav.addObject("completed_tickets", completedTickets);
        mav.addObject("completed_count",completedPending);
        
        
        return mav;
    }

}