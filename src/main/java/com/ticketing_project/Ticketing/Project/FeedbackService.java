package com.ticketing_project.Ticketing.Project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

	@Autowired
	private FeedbackRepository repo;

	public Feedback save(Feedback feedback) {
		return repo.save(feedback);
	}

	public List<Feedback> getFeedbacksByStatusId(int statusId) {
		return repo.findAllByStatusId(statusId);
	}
}
