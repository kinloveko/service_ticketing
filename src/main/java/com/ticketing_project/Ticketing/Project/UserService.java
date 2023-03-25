package com.ticketing_project.Ticketing.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	
	public void save(User user) {
		repo.save(user);
	}
	
}
