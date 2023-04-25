package com.ticketing_project.Ticketing.Project;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Integer>{
	
	 Invoice findByticketID(int ticketID);
	
}
