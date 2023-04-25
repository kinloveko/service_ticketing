package com.ticketing_project.Ticketing.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

	List<Ticket> findByStatus(String status);

	List<Ticket> findByTitle(String title);

}