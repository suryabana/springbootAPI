package com.spring.repository;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.model.Patient;
import com.spring.model.PatientInformation;

public interface PatientInformationRepository extends JpaRepository<PatientInformation, Long> {
	
	PatientInformation findByEmail(String email);

	PatientInformation findByIdAndEmail(long id, String email);
	
	void deleteById(long Id);
	
	void deleteByEmail(String email);
	
	void deleteByPatient(Patient patient);
	
	PatientInformation findByPatient(Patient patient);


}
