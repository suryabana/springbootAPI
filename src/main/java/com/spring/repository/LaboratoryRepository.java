package com.spring.repository;

import java.util.List;

import org.hibernate.annotations.Any;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.model.Laboratory;
import com.spring.model.Patient;

public interface LaboratoryRepository extends JpaRepository<Laboratory , String> {
	
	List<Laboratory> findAll();

	Laboratory findByLabId(String labId);

	void deleteByLabId(String labId);
}
