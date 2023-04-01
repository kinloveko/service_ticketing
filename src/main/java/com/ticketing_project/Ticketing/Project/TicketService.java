package com.ticketing_project.Ticketing.Project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository repo;
	
	
	public void save(Ticket ticket) {
		repo.save(ticket);
	}
  
	public Ticket getTicketById(int ticketId) {
		List<Ticket> matchingTicket = getAllTickets().stream()
				.filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();
		
		if(matchingTicket.size() == 0) return null;
		
		return matchingTicket.get(0);
	}
	
	public List<Ticket> getTicketsByStatus(String status){
		final List<Ticket> filteredTicketList = repo.findByStatus(status);
		return filteredTicketList;
	}
	
	public List<Ticket> getTicketsByStatusAndUserId(int userId, String status){
		final List<Ticket> filteredTicketList = repo
				.findAll()
				.stream()
				.filter(ticket -> ticket.getStatus().equals(status) && ticket.getUser_id() == userId)
				.toList();
		return filteredTicketList;
	}
	
	public List<Ticket> getTicketsByUserId(int user_id){
		return repo.findAll()
				.stream()
				.filter(ticket -> ticket.getUser_id() == user_id)
				.toList();
	}
}
