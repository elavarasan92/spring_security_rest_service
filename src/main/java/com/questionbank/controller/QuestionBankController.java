package com.questionbank.controller;

import com.questionbank.error.BookNotFoundException;
import com.questionbank.error.BookUnSupportedFieldPatchException;
import com.questionbank.exception.ResourceNotFoundException;
import com.questionbank.model.Book;
import com.questionbank.model.Course;
import com.questionbank.model.Subject;
import com.questionbank.model.User;
import com.questionbank.model.Year;
import com.questionbank.repository.BookRepository;
import com.questionbank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.questionbank.service.QuestionBankService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*",allowCredentials="true",exposedHeaders="true")
@Api(value = "Question bank System", description = "Question bank")
public class QuestionBankController {

	@Autowired
	private QuestionBankService questionBankService;

	@Autowired
	private BookRepository repository;

	@Autowired
	private UserRepository userRepository;

	@ApiOperation(value = "Login", authorizations = @Authorization(value = "basic"))
	@GetMapping("/login")
	public ResponseEntity<User> currentUserName(Authentication authentication) throws ResourceNotFoundException {
		HttpHeaders responseHeaders = new HttpHeaders();
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		System.out.println("Session id : "+sessionId);
		responseHeaders.set("JSESSIONID", sessionId);
		User user = userRepository.getUserByName(authentication.getName());
		if (user == null) {
			new ResourceNotFoundException("User not found :: " + authentication.getName());
		}else {
			user.setJsessionId(sessionId);
		}
		return ResponseEntity.ok()
			      .headers(responseHeaders).body(user);
	}

	@PostMapping("/registerUser")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) { 
		User userDet = userRepository.getUserByName(user.getUsername());
		if (userDet == null) {
			userDet = questionBankService.registerUser(user);
			return ResponseEntity.ok().body(userDet);
		} else {
			System.out.println("User creation failed");
			return null;
		}
	}
	
	@GetMapping("/auth/users")
	public List<User> getAllUsers() {
		return questionBankService.getAllUsers();
	}

	@GetMapping("/auth/{username}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "username") String username)
			throws ResourceNotFoundException {
		User user = userRepository.getUserByName(username);
		if (user == null) {
			new ResourceNotFoundException("User not found :: " + username);
		}
		return ResponseEntity.ok().body(user);
	}

	

	@PutMapping("/auth/{username}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "username") String username,
			@Valid @RequestBody User userDetails) throws ResourceNotFoundException {
		User user = userRepository.getUserByName(username);
		if (user == null) {
			new ResourceNotFoundException("User not found :: " + username);
		}
		user.setEmailAddress(userDetails.getEmailAddress());
		user.setLastName(userDetails.getLastName());
		user.setFirstName(userDetails.getFirstName());
		user.setUpdatedDate(new Timestamp(new Date().getTime()));
		final User updatedUser = questionBankService.updateUser(user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/auth/{username}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "username") String username)
			throws ResourceNotFoundException {
		User user = userRepository.getUserByName(username);
		if (user == null) {
			new ResourceNotFoundException("User not found :: " + username);
		}
		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@GetMapping("/auth/courses")
	public List<Course> getAllCourses() {
		return questionBankService.getAllCourses();
	}

	@PostMapping("/auth/addcourse")
	public Course addCourse(@Valid @RequestBody Course course) {
		return questionBankService.addCourse(course);
	}

	@PutMapping("/auth/{id}")
	public ResponseEntity<Course> update(@PathVariable Long id, @Valid @RequestBody Course course) {
		if (!questionBankService.findById(id).isPresent()) {
			System.out.println("Id " + id + " is not existed");
			ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(questionBankService.save(course));
	}

	@PostMapping("/auth/addcourses")
	public List<Course> addCourses(@Valid @RequestBody List<Course> courseList) {
		return questionBankService.addCourses(courseList);
	}

	@GetMapping("/auth/years/courseid/{courseid}")
	public List<Year> getYearsByCourseId(@PathVariable(value = "courseid") Long courseID)
			throws ResourceNotFoundException {
		return questionBankService.getYearsByCourseId(courseID);
	}

	@PostMapping("/auth/addyears")
	public List<Year> addYearsForCourses(@Valid @RequestBody List<Year> yearList) {
		return questionBankService.addYearsForCourses(yearList);
	}

	@GetMapping("/auth/subjects/yearid/{yearid}")
	public List<Subject> getSubjectsByYearId(@PathVariable(value = "yearid") Long yearID)
			throws ResourceNotFoundException {
		return questionBankService.getSubjectsByYearId(yearID);
	}

	@PostMapping("/auth/addsubjects")
	public List<Subject> addSubjectForYears(@RequestBody List<Subject> subjectList) {
		return questionBankService.addSubjectForYears(subjectList);
	}

	@GetMapping("/auth/years")
	public List<Year> getAllYears(long courseId) {
		return questionBankService.getAllYears();
	}

	@GetMapping("/auth/subjects")
	public List<Subject> getAllSubjects() {
		return questionBankService.getAllSubjects();
	}

	/*// Find
	@GetMapping("/auth/books")
	List<Book> findAll() {
		return repository.findAll();
	}

	@GetMapping("/auth/bookAdmin")
	List<Book> findBooks() {
		return repository.findAll();
	}

	// Save
	@PostMapping("/auth/books")
	@ResponseStatus(HttpStatus.CREATED)
	Book newBook(@Valid @RequestBody Book newBook) {
		return repository.save(newBook);
	}

	// Find
	@GetMapping("/auth/books/{id}")
	Book findOne(@PathVariable @Min(1) Long id) {
		return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
	}

	// Save or update
	@PutMapping("/auth/books/{id}")
	Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {

		return repository.findById(id).map(x -> {
			x.setName(newBook.getName());
			x.setAuthor(newBook.getAuthor());
			x.setPrice(newBook.getPrice());
			return repository.save(x);
		}).orElseGet(() -> {
			newBook.setId(id);
			return repository.save(newBook);
		});
	}

	// update author only
	@PatchMapping("/auth/books/{id}")
	Book patch(@RequestBody Map<String, String> update, @PathVariable Long id) {

		return repository.findById(id).map(x -> {

			String author = update.get("author");
			if (!StringUtils.isEmpty(author)) {
				x.setAuthor(author);

				// better create a custom method to update a value = :newValue where id = :id
				return repository.save(x);
			} else {
				throw new BookUnSupportedFieldPatchException(update.keySet());
			}

		}).orElseGet(() -> {
			throw new BookNotFoundException(id);
		});

	}

	@DeleteMapping("/auth/books/{id}")
	void deleteBook(@PathVariable Long id) {
		repository.deleteById(id);
	}*/

}
