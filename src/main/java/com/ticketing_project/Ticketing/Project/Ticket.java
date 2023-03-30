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
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ticket_id", nullable=false)
	@Getter
	@Setter
	private int ticket_id;
	
	@Getter
	@Setter
	@Column(name="title", nullable=false)
	private String title;
	
	@Getter
	@Setter
	@Column(name="description", nullable=false)
	private String description;
	

	@Getter
	@Setter
	@Column(name="status", nullable=false)
	private String status;
	
	@Getter
	@Setter
	@CreationTimestamp
    @Column(name="created_on", nullable=false, updatable=false)
	private Timestamp created_on;

	@Getter
	@Setter
	@Column(name="userId", nullable=false, updatable=false)
	private int userId;
}

