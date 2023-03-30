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
	
	public List<Ticket> getAllTickets()
	{
		final List<Ticket> ticketList = repo.findAll();
		return ticketList;
	}
	
	public List<Ticket> getTicketsByStatus(String status){
		final List<Ticket> filteredTicketList = repo.findByStatus(status);
		return filteredTicketList;
	}
	
	public List<Ticket> getTicketsByStatusAndUserId(int userId, String status){
		final List<Ticket> filteredTicketList = repo
				.findAll()
				.stream()
				.filter(ticket -> ticket.getStatus().equals(status) && ticket.getUserId() == userId)
				.toList();
		return filteredTicketList;
	}
	
	public List<Ticket> getTicketsByUserId(int userId){
		return repo.findAll()
				.stream()
				.filter(ticket -> ticket.getUserId() == userId)
				.toList();
	}
}
