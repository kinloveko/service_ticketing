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
	@Column(name="user_name", nullable=false)
	private String user_name;
	
	@Getter
	@Setter
	@Column(name="title", nullable=false)
	private String title;
	
	@Getter
	@Setter
	@Column(name="description", nullable=false, updatable=true)
	private String description;
	
	@Getter
	@Setter
	@Column(name="status")
	private String status="pending";
	
	@Getter
	@Setter
	@CreationTimestamp
    @Column(name="created_on", nullable=false, updatable=false)
	private Timestamp created_on;

	@Getter
	@Setter
	@Column(name="user_id", updatable=false)
	private int user_id;
	
	@Getter
	@Setter
	@Column(name="conforme_no", updatable=false)
	private int conforme_no = Integer.parseInt((ticket_id+user_id) + String.valueOf(System.currentTimeMillis()).substring(6));

	@Getter
	@Setter
	@Column(name="amount",nullable=false, updatable=false)
	private String amount="";
	
	@Getter
	@Setter
	@Column(name="image_signature_link", nullable=false, updatable=false)
	private String image_signature_link="";
	
	@Getter
	@Setter
	@CreationTimestamp
    @Column(name="conforme_date", nullable=false, updatable=false)
	private Timestamp conforme_date;
	
	@Getter
	@Setter
	@Column(name="progress",nullable=false, updatable=false)
	private String progress="";
	
}

