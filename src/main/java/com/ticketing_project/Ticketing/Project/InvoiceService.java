package com.ticketing_project.Ticketing.Project;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class InvoiceService {

	@Autowired
	private InvoiceRepository repo;
	
	
	public Invoice save(Invoice invoice) {
		return repo.save(invoice);
	}
	
	public Optional<Invoice> findByTicketID(int ticketID) {
		return repo.findByticketID(ticketID);
	}
	
		
	
}
