package com.ticketing_project.Ticketing.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByUserRole(String userRole);

    @Query("SELECT u FROM User u WHERE u.user_email = ?1")
    User findByUserEmail(String email);
    
    
    @Query("SELECT u FROM User u WHERE u.user_email = ?1 AND u.user_password = ?2")
    User findByUserEmailAndPassword(String userEmail, String userPassword);

}