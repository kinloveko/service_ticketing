package com.ticketing_project.Ticketing.Project;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	
	public void save(User user) {
		repo.save(user);
	}
	
	public List<User> getAllUsers()
	{
		final List<User> userList = repo.findAll();
		return userList;
	}
	
}
