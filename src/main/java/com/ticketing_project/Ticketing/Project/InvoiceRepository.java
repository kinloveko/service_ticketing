package com.ticketing_project.Ticketing.Project;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Integer>{
	
	 Optional<Invoice> findByticketID(int ticketID);
	
}
