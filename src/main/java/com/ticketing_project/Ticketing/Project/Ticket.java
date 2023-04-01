package com.ticketing_project.Ticketing.Project;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="tickets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Ticket {
<<<<<<< HEAD
	
=======
>>>>>>> 017941997f54cde4866ef63ecd3a7de9b25d2673
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ticket_id", nullable=false)
	@Getter
	@Setter
	private int ticket_id;
	
	@Getter
	@Setter
	@Column(name="user_name", nullable=false)
	private String user_name;
	
	@Getter
	@Setter
	@Column(name="title", nullable=false)
	private String title;
	
	@Getter
	@Setter
<<<<<<< HEAD
	@Column(name="description", nullable=false, updatable=true)
=======
	@Column(name="description", nullable=false)
>>>>>>> 017941997f54cde4866ef63ecd3a7de9b25d2673
	private String description;
	

	@Getter
	@Setter
	@Column(name="status")
<<<<<<< HEAD
	private String status="pending";
=======
	private String status="Pending";
>>>>>>> 017941997f54cde4866ef63ecd3a7de9b25d2673
	
	@Getter
	@Setter
	@CreationTimestamp
    @Column(name="created_on", nullable=false, updatable=false)
	private Timestamp created_on;

	@Getter
	@Setter
	@Column(name="user_id", updatable=false)
	private int user_id;
	
<<<<<<< HEAD
	@Getter
	@Setter
	@Column(name="conforme_no",nullable=false, updatable=false)
	private String conforme_no="";
	
	
=======
>>>>>>> 017941997f54cde4866ef63ecd3a7de9b25d2673
}

