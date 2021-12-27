package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.model.Address;
import com.spring.model.User;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {

	Address findById(long id);
	List<Address> findByCity(String city);
	List<Address> findByState(String state);
	List<Address> findByCountry(String country);
	List<Address> findByZipCode(long zipcode);
	Address findByPhoneNumber(String phoneNumber);
}
