package com.ticketing_project.Ticketing.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer>{

	List<Feedback> findAllByStatusId(int statusId);

}
