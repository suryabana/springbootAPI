package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.model.Tests;

public interface TestsRepository extends JpaRepository<Tests, Long>{
	
	List<Tests> findAll();

	Tests findByTestId(long testId);

	void deleteByTestId(long testId);
}
