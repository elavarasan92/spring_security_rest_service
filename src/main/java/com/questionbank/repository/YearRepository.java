package com.questionbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.questionbank.model.Year;

@Repository
public interface YearRepository extends JpaRepository<Year, Long>{

	 @Query("select y from Year y  where y.courseID = :courseID")
	 List<Year> getYearsByCourseId(@Param("courseID") Long courseID);
	 

}
