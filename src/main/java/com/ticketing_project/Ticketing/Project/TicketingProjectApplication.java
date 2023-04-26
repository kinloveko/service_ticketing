package com.ticketing_project.Ticketing.Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicketingProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingProjectApplication.class, args);
	}

	@Bean(name = "USER_BEAN")
	public User setUser() {
		User u = new User();
		u.setUser_name("name@gmail.com");
		u.setUser_password("1234");
		return u;
	}
}
