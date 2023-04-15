package com.ticketing_project.Ticketing.Project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
	
	@Autowired
	private StatusRepository repo;
	
	
	
	public Status save(Status status) {
		return repo.save(status);
	}
	
	public List<Status> getStatusById(int id)
	{
		List<Integer> ids = new ArrayList<>();
		ids.add(id);
		final List<Status> userList = repo.findAllById(ids);
		return userList;
	}
	
	public List<Status> getAllStatus()
	{
		final List<Status> userList = repo.findAll();
		return userList;
	}
	
}
