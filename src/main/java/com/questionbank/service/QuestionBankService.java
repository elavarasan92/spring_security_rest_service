package com.questionbank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.questionbank.model.Course;
import com.questionbank.model.Subject;
import com.questionbank.model.User;
import com.questionbank.model.Year;
import com.questionbank.repository.CourseRepository;
import com.questionbank.repository.SubjectRepository;
import com.questionbank.repository.UserRepository;
import com.questionbank.repository.YearRepository;

@Service
public class QuestionBankService {

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private YearRepository yearRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

	public @Valid Course addCourse(@Valid Course course) {
		return courseRepository.save(course);
	}

	public List<Course> addCourses(@Valid List<Course> courseList) {
		List<Course> courses = courseRepository.saveAll(courseList);
		
		for(Course course : courses) {
			List<Year> years = new ArrayList<Year>();
			
			for(int i=1;i<=course.getNoOfYears() ;i++) {
				Year year = new Year();
				year.setCourseID(course.getCourseID());
		        String yearName; 
		        switch (i) { 
		        case 1: 
		            yearName = "FIRST_YEAR"; 
		            break; 
		        case 2: 
		            yearName = "SECOND_YEAR"; 
		            break; 
		        case 3: 
		            yearName = "THIRD_YEAR"; 
		            break; 
		        case 4: 
		            yearName = "FOURTH_YEAR"; 
		            break; 
		        case 5: 
		            yearName = "FIFTH_YEAR"; 
		            break; 
		        default: 
		            yearName = "INVALID_YEAR"; 
		            break; 
		        } 
		        System.out.println(yearName); 
				year.setCreatedBy(course.getCreatedBy());
				year.setYearName(yearName);
				years.add(year);
			}
			course.setYears(yearRepository.saveAll(years));
		}
		return courses;
	}
	
	public List<Year> getYearsByCourseId(Long courseID) {
		return yearRepository.getYearsByCourseId(courseID);
	}
	
	public List<Year> addYearsForCourses(@Valid List<Year> yearList) {
		return yearRepository.saveAll(yearList);
	}
	
	public List<Subject> addSubjectForYears(@Valid List<Subject> subjectList) {
		return subjectRepository.saveAll(subjectList);
	}


	public List<Subject> getSubjectsByYearId(Long yearID) {
		return subjectRepository.getSubjectsByYearId(yearID);
	}
	public List<Year> getAllYears() {
		return yearRepository.findAll();
	}
	
	public List<Subject> getAllSubjects() {
		return subjectRepository.findAll();
	}

	public Optional<Course> findById(Long id) {
		return courseRepository.findById(id);
	}

	public Course save(@Valid Course course) {
		return courseRepository.save(course);
	}

	public User registerUser(@Valid User user) {
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	
	public User checkUser(String username , String password) {
		return userRepository.checkUser(username,password);
	}

	

	
}
