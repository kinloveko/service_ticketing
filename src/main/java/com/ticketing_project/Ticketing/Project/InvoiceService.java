package com.ticketing_project.Ticketing.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

	@Autowired
	private InvoiceRepository repo;
	
	
	public Invoice save(Invoice invoice) {
		return repo.save(invoice);
	}
	
	
	
	
}
