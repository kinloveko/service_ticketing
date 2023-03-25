package com.ticketing_project.Ticketing.Project;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int user_id;
	 String user_email;
	 String user_name;
	 String user_password;
	 String user_role;
	 
}


