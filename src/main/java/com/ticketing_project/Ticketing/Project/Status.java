package com.ticketing_project.Ticketing.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="status")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="status_id", nullable=false)
	@Getter
	@Setter
	private int status_id;
	
	@Column(name="ticketID", nullable=false)
	@Getter
	@Setter
	private int ticketID;
	
	
	@Column(name="status_title", nullable=false)
	@Getter
	@Setter
	private String status_title;
	
	@Column(name="status_message", nullable=false)
	@Getter
	@Setter
	private String status_message;
	
	
	@Column(name="status_date_time", nullable=false)
	@Getter
	@Setter
	private String status_date_time;
	
	
	@Column(name="status_progress", nullable=false)
	@Getter
	@Setter
	private String status_progress;
	
	
}
