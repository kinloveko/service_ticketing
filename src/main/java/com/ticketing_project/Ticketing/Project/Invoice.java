package com.ticketing_project.Ticketing.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Table(name="Invoice")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Invoice {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="invoice_id", nullable=false)
	@Getter
	@Setter
	private int invoice_id;
	
	
	@Column(name="invoice_date", nullable=false)
	@Getter
	@Setter
	private String invoice_date;
	
	@Column(name="invoice_amount", nullable=false)
	@Getter
	@Setter
	private int invoice_amount;

	
	@Column(name="ticket_issue_title", nullable=false)
	@Getter
	@Setter
	private String ticket_issue_title;
	
	
	@Column(name="ticketID", nullable=false)
	@Getter
	@Setter
	private int ticketID;
	
	
	@Column(name="client_payment_proof", nullable=false)
	@Getter
	@Setter
	private String client_payment_proof;
	
	@Column(name="client_signature", nullable=false)
	@Getter
	@Setter
	private String client_signature;
	
	
	@Column(name="client_name", nullable=false)
	@Getter
	@Setter
	private String client_name;

	@Column(name="client_email", nullable=false)
	@Getter
	@Setter
	private String client_email;
	
	
	@Column(name="payment_confirmation", nullable=false)
	@Getter
	@Setter
	private String paymentConfirmation;
	
	
}
