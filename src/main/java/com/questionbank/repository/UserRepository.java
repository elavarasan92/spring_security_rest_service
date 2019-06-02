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

}
