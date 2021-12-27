package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Doctor;

@Repository
@Transactional
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	Doctor findByDoctorId(long doctorId);

	void deleteByDoctorId(long doctorId);
}
