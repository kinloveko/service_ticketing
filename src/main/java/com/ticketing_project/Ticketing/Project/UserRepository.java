package com.ticketing_project.Ticketing.Project;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByUserRole(String userRole);
	
}