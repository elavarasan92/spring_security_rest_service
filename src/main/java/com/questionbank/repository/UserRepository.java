package com.questionbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.questionbank.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
	public User getUserByName(@Param("username") String username);

	@Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username) and LOWER(u.password) = LOWER(:password)")
	public User checkUser(@Param("username") String username, @Param("password") String password);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.emailAddress) = LOWER(:emailAddress)")
	public User getUserByEmail(@Param("emailAddress") String emailAddress);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.otp) = LOWER(:otp)")
	public User getUserByOTP(@Param("otp") String otp);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.emailAddress) = LOWER(:emailAddress) and LOWER(u.otp) = LOWER(:otp)")
	public User validateOTP(String emailAddress, String otp);
	
	

}
