package com.questionbank.controller;

import com.questionbank.exception.GlobalException;
import com.questionbank.exception.ResourceAlreadyExistException;
import com.questionbank.exception.ResourceNotFoundException;
import com.questionbank.model.Course;
import com.questionbank.model.Subject;
import com.questionbank.model.User;
import com.questionbank.model.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.questionbank.service.QuestionBankService;
import com.questionbank.util.QUtils;
import com.questionbank.util.SendEmailTLS;

import io.swagger.annotations.Api;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*",allowCredentials="true",exposedHeaders="true")
@Api(value = "Question bank System", description = "Question bank")
public class QuestionBankController {

	@Autowired
	private QuestionBankService questionBankService;

	
	@GetMapping("/login")
	public ResponseEntity<User> login(Authentication authentication) throws ResourceNotFoundException {
		HttpHeaders responseHeaders = new HttpHeaders();
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		System.out.println("Session id : "+sessionId);
		responseHeaders.set("JSESSIONID", sessionId);
		User user = questionBankService.getUserByName(authentication.getName());
		if (user == null) {
			throw new ResourceNotFoundException("USER_NOT_FOUND");
		}
		return ResponseEntity.ok()
			      .headers(responseHeaders).body(user);
	}

	@PostMapping("/registerUser")
	public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws ResourceAlreadyExistException{ 
		User userDet =  questionBankService.getUserByName(user.getUsername());
		if (userDet == null) {
			userDet = questionBankService.registerUser(user);
			return ResponseEntity.ok().body(userDet);
		} else {
			System.out.println("User creation failed");
			throw new ResourceAlreadyExistException("USER_ALREADY_EXIST");
		}
	}
	
	@GetMapping("/forgotPassword/{emailId}")
	public ResponseEntity<String> forgotPassword(@PathVariable(value = "emailId") String emailId) throws GlobalException { 
		User userDet = questionBankService.getUserByEmail(emailId);
		if (userDet != null) {
			String otp = QUtils.getOTP();
			userDet.setOtp(otp);
			questionBankService.updateUser(userDet);
			SendEmailTLS.sendOTP(emailId,otp);
			return ResponseEntity.ok().body("OTP_SENT_SUCCESSFULLY");
		} else {
			System.out.println("OTP creation failed");
			throw new GlobalException("EMAIL_DOESNOT_EXIST");
		}
	}
	
	
	@GetMapping("/validateOTP/{emailId}/{otp}")
	public ResponseEntity<String> validateOTP(@PathVariable(value = "emailId") String emailId,@PathVariable(value = "otp") String otp) throws GlobalException { 
		User userDet = questionBankService.validateOtp(emailId,otp);
		if (userDet != null) {
			return ResponseEntity.ok().body("VALID_OTP");
		} else {
			System.out.println("OTP validation failed");
			throw new GlobalException("INVALID_OTP");
		}
	}
	
	@GetMapping("/resetPassword/{emailId}/{password}")
	public ResponseEntity<String> resetPassword(@PathVariable(value = "emailId") String emailId,@PathVariable(value = "password") String password) throws GlobalException { 
		User userDet = questionBankService.getUserByEmail(emailId);
		if (userDet != null) {
			userDet.setPassword(password);
			questionBankService.updateUser(userDet);
			return ResponseEntity.ok().body("PASSWORD_UPDATED_SUCCESSFULLY");
		} else {
			System.out.println("OTP creation failed");
			throw new GlobalException("EMAIL_DOESNOT_EXIST");
		}
	}
	
	
	@GetMapping("/auth/getAllUsers")
	public List<User> getAllUsers() {
		return questionBankService.getAllUsers();
	}

	@GetMapping("/auth/getUserById/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "userId") Long userId)
			throws ResourceNotFoundException {
		Optional<User> user = questionBankService.getUserById(userId);
		if (!user.isPresent()) {
			throw	new ResourceNotFoundException("USER_NOT_FOUND");
		}
		return ResponseEntity.ok().body(user.get());
	}

	

	@PutMapping("/auth/updateUser/{username}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "username") String username,
			@Valid @RequestBody User userDetails) throws ResourceNotFoundException {
		User user = questionBankService.getUserByName(username);
		if (user == null) {
			throw	new ResourceNotFoundException("USER_NOT_FOUND");
		}
		user.setEmailAddress(userDetails.getEmailAddress());
		user.setLastName(userDetails.getLastName());
		user.setFirstName(userDetails.getFirstName());
		user.setUpdatedDate(new Timestamp(new Date().getTime()));
		final User updatedUser = questionBankService.updateUser(user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/auth/deleteUser/{username}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "username") String username)
			throws ResourceNotFoundException {
		User user = questionBankService.getUserByName(username);
		if (user == null) {
			throw	new ResourceNotFoundException("USER_NOT_FOUND");
		}
		questionBankService.deleteUserByName(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@GetMapping("/auth/getAllCourses")
	public List<Course> getAllCourses() {
		return questionBankService.getAllCourses();
	}


	@PutMapping("/auth/updateCourse/{id}")
	public ResponseEntity<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody Course course)throws  ResourceNotFoundException{
		if (!questionBankService.checkCourse(id).isPresent()) {
			System.out.println("Id " + id + " is not existed");
			throw new ResourceNotFoundException("COUSRSE_NOT_FOUND");
		}
		return ResponseEntity.ok(questionBankService.save(course));
	}

	@PostMapping("/auth/addcourses")
	public List<Course> addCourses(@Valid @RequestBody List<Course> courseList) {
		return questionBankService.addCourses(courseList);
	}

	@GetMapping("/auth/getYearsByCourseId/{courseid}")
	public List<Year> getYearsByCourseId(@PathVariable(value = "courseid") Long courseID)
			throws ResourceNotFoundException {
		List<Year> years = questionBankService.getYearsByCourseId(courseID);
		if (years == null) {
			throw	new ResourceNotFoundException("COURSE_ID_DOESNOT_EXIST");
		}
		return years;
	}

	@PostMapping("/auth/addYearsForCourses")
	public List<Year> addYearsForCourses(@Valid @RequestBody List<Year> yearList) {
		return questionBankService.addYearsForCourses(yearList);
	}

	@GetMapping("/auth/getSubjectsByYearId/{yearid}")
	public List<Subject> getSubjectsByYearId(@PathVariable(value = "yearid") Long yearID)
			throws ResourceNotFoundException {
		List<Subject> subjects =  questionBankService.getSubjectsByYearId(yearID);
		if (subjects == null) {
			throw	new ResourceNotFoundException("YEAR_ID_DOESNOT_EXIST");
		}
		return subjects;
	}

	@PostMapping("/auth/addSubjectForYears")
	public List<Subject> addSubjectForYears(@Valid @RequestBody List<Subject> subjectList) {
		return questionBankService.addSubjectForYears(subjectList);
	}

	@GetMapping("/auth/getAllYears")
	public List<Year> getAllYears(long courseId) {
		return questionBankService.getAllYears();
	}

	@GetMapping("/auth/getAllSubjects")
	public List<Subject> getAllSubjects() {
		return questionBankService.getAllSubjects();
	}
	
	@PutMapping("/auth/updateSubject/{id}")
	public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @Valid @RequestBody Subject subject)throws ResourceNotFoundException{
		if (!questionBankService.checkSubject(id).isPresent()) {
			System.out.println("Id " + id + " is not existed");
			throw new ResourceNotFoundException("COUSRSE_NOT_FOUND");
		}
		return ResponseEntity.ok(questionBankService.saveSubject(subject));
	}

	@DeleteMapping("/auth/deleteSubject/{id}")
	public Map<String, Boolean> deleteSubject(@PathVariable Long id)
			throws ResourceNotFoundException {
		Optional<Subject> subject = questionBankService.getSubjectById(id);
		if (subject == null) {
			throw new ResourceNotFoundException("SUBJECT_ID_DOESNOT_EXIST");
		}
		questionBankService.deleteSubject(subject.get());
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
	
	@DeleteMapping("/auth/deleteCourse/{id}")
	public Map<String, Boolean> deleteCourse(@PathVariable Long id)
			throws ResourceNotFoundException {
		Optional<Course> course = questionBankService.getCourseById(id);
		if (course == null) {
			throw new ResourceNotFoundException("COURSE_ID_DOESNOT_EXIST");
		}
		questionBankService.deleteCourse(course.get());
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
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
