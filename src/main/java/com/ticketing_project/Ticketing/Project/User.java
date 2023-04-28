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
@Table(name = "user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", nullable = false)
	@Getter
	@Setter
	private int user_id;

	@Getter
	@Setter
	@Column(name = "user_email", nullable = false)
	private String user_email;

	@Getter
	@Setter
	@Column(name = "user_name", nullable = false)
	private String user_name;
	
	@Getter
	@Setter
	@Column(name = "user_password", nullable = false)
	private String user_password;
	
	@Getter
	@Setter
	@Column(name = "contactNumber", nullable = true)
	private String contactNumber;
	
	@Getter
	@Setter
	@Column(name = "address", nullable = true)
	private String address;

	@Getter
	@Setter
	@Column(name = "user_role", nullable = false)
	private String userRole;

	@Getter
	@Setter
	@Column(name = "status", nullable = false)
	private String status;

	@Getter
	@Setter
	@Column(name = "profileImage", nullable = true)
	private String profileImage;
	
	@Getter
	@Setter
	@Column(name = "token", nullable = false)
	private String token;
	
	
}
