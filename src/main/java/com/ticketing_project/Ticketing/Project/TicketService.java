package com.ticketing_project.Ticketing.Project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository repo;
		
	
	
	public Ticket save(Ticket ticket) {
		return repo.save(ticket);
	}
	
	
	public void update(int ticketId, Ticket updatedTicket) {
		//List containing the ticket that matches the ticketId
		List<Ticket> matchingTicket = getAllTickets().stream()
				.filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();
		
		if(matchingTicket.size() != 0) {
			if (updatedTicket.getTitle() != null) {
				matchingTicket.get(0).setTitle(updatedTicket.getTitle());
		    }
		    if (updatedTicket.getDescription() != null) {
		    	matchingTicket.get(0).setDescription(updatedTicket.getDescription());
		    }
		    if (updatedTicket.getStatus() != null) {
		    	matchingTicket.get(0).setStatus(updatedTicket.getStatus());
		    }
		    if (updatedTicket.getUser_id() != 0) {
		    	matchingTicket.get(0).setUser_id(updatedTicket.getUser_id());
		    }
		    
		    repo.save(matchingTicket.get(0));
		}
	}
	
	public void delete(int ticketId) {
		//List containing the ticket that matches the ticketId
		List<Ticket> matchingTicket = getAllTickets().stream()
				.filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();
		
		if(matchingTicket.size() != 0) {
		    repo.delete(matchingTicket.get(0));
		}
	}
	
	public List<Ticket> getAllTickets()
	{
		final List<Ticket> ticketList = repo.findAll();
		return ticketList;
	}
	
	
	public Ticket getTicketById(int ticketId) {
		List<Ticket> matchingTicket = repo.findAll().stream()
				.filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();
		
		if(matchingTicket.size() == 0) return null;
		
		return matchingTicket.get(0);
	}
	
	public List<Ticket> getTicketsByUserId(int user_id){
		return repo.findAll()
				.stream()
				.filter(ticket -> ticket.getUser_id() == user_id)
				.toList();
	}
	
	
	  public void populateTicketModel(Model m) {
	        List<Ticket> list = getAllTickets();
	        
	        int pendingCount = 0;
	        int completedCount = 0;
	        int ongoingCount = 0;
	        int supportCount = 0;
	        
	        List<Ticket> pendingTickets = new ArrayList<>();
	        List<Ticket> ongoingTickets = new ArrayList<>();
	        List<Ticket> completedTickets = new ArrayList<>();
	        List<Ticket> supportTickets = new ArrayList<>();
	 
	        
	        for(Ticket t : list) {
	            if(t.getStatus().equals("pending")) {
	                pendingTickets.add(t);
	                pendingCount++;
	            }
	            else if(t.getStatus().equals("ongoing")) {
	                ongoingTickets.add(t);
	                ongoingCount++;
	            }
	            else if(t.getStatus().equals("completed")) {
	                completedTickets.add(t);
	                completedCount++;
	            }
	            if(t.getProgress()!=null) {
	            	if(t.getProgress().equals("support_team")) {
		        		supportCount++;
		        		supportTickets.add(t);
		        	}	
	            }
	        
	        }
	        
	        m.addAttribute("pending_tickets", pendingTickets);
	        m.addAttribute("pending_ticket_count", pendingCount);
	        
	        m.addAttribute("completed_tickets", completedTickets);
	        m.addAttribute("completed_ticket_count", completedCount);
	        
	        m.addAttribute("ongoing_tickets", ongoingTickets);
	        m.addAttribute("ongoing_ticket_count", ongoingCount);
	        
	        m.addAttribute("support_tickets", supportTickets);
	        m.addAttribute("support_count",supportCount);
	        
	        
	    }
	
	
}