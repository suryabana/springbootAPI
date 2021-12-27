package com.spring.repository;

import java.util.List;

import org.hibernate.annotations.Any;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	
	Patient findByPatientId(long patientId);
	
	Patient findByMobileNumber(String mobileNumber);

	void deleteByPatientId(long patientId);
	
	void deleteByMobileNumber(String mobileNumber);
	
	void deleteByFirstNameAndLastName(String firstName, String lastname);

	List<Patient> findByFirstNameAndLastName(String firstName, String lastName);
}
