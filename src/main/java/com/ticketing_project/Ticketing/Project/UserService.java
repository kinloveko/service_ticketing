package com.ticketing_project.Ticketing.Project;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	public void save(User user) {
		repo.save(user);
	}

	public Optional<User> findUserById(int user_id) {
		return repo.findById(user_id);
	}

	public List<User> getAllUsers() {
		final List<User> userList = repo.findAll();
		return userList;
	}

	public User findByUserEmail(String email) {
		return repo.findByUserEmail(email);
	}

	public User loginUser(String email, String password) {
		return repo.findByUserEmailAndPassword(email, password);
	}

	public void deleteUser(int userID) {
		// List containing the ticket that matches the ticketId
		List<User> user = getAllUsers().stream().filter(userIDs -> userIDs.getUser_id() == userID).toList();

		if (user.size() != 0) {
			repo.delete(user.get(0));
		}
	}

}
