package com.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.constants.ResponseCode;
import com.spring.constants.WebConstants;
import com.spring.model.Doctor;
import com.spring.model.Laboratory;
import com.spring.model.Patient;
import com.spring.model.Tests;
import com.spring.model.User;
import com.spring.repository.DoctorRepository;
import com.spring.repository.LaboratoryRepository;
import com.spring.repository.PatientRepository;
import com.spring.repository.TestsRepository;
import com.spring.repository.UserRepository;
import com.spring.response.doctorResp;
import com.spring.response.labResp;
import com.spring.response.patientResp;
import com.spring.response.prodResp;
import com.spring.response.serverResp;
import com.spring.response.testResp;
import com.spring.util.Validator;
import com.spring.util.jwtUtil;

import io.swagger.annotations.Api;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("api/home")
@Api(value = "API to Maintain the Hospital Data by different inputs of Data",
description = "This API provides the capability to Add , Delete or modify the hospital Data", produces = "application/json")
public class HomeController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DoctorRepository doctorRepo;
	
	@Autowired
	private LaboratoryRepository labRepo;
	
	@Autowired
	private jwtUtil jwtutil;
	
	@PostMapping("/registerUser")
	public ResponseEntity<serverResp> addUser(@Valid @RequestBody User user) {
		serverResp resp = new serverResp();
		if (Validator.isUserEmpty(user)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else {
			try {
					user.setUsertype(WebConstants.USER_PATIENT);
					User  result = userRepo.saveAndFlush(user);
					String jwtToken = jwtutil.createToken(user.getEmail(), user.getPassword(), WebConstants.USER_PATIENT);
					resp.setStatus(ResponseCode.SUCCESS_CODE);
					resp.setMessage(ResponseCode.SUCCESS_MESSAGE);
					resp.setAUTH_TOKEN(jwtToken);
					resp.setEmail(user.getEmail());
					resp.setUserName(result.getUsername());
					resp.setUserType(result.getUsertype());		
			} catch (Exception e) {
					resp.setStatus(ResponseCode.FAILURE_CODE);
					resp.setMessage(e.getMessage());
			}
		}
		
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}
	
	
	
	@PostMapping("/login") 
	public ResponseEntity<serverResp> verifyUser(@Valid @RequestBody HashMap<String, String> credential) {
		String email = "";
		String password = "";
		if (credential.containsKey(WebConstants.USER_EMAIL)) {
			email = credential.get(WebConstants.USER_EMAIL);
		}
		if (credential.containsKey(WebConstants.USER_PASSWORD)) {
			password = credential.get(WebConstants.USER_PASSWORD);
		}
		User loggedUser = userRepo.findByEmailAndPassword(email, password);
		serverResp resp = new serverResp();
		if (loggedUser != null) {
			String jwtToken = jwtutil.createToken(email, password, loggedUser.getUsertype());
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.SUCCESS_MESSAGE);
			resp.setAUTH_TOKEN(jwtToken);
			resp.setEmail(loggedUser.getEmail());
			resp.setUserName(loggedUser.getUsername());
			resp.setUserType(loggedUser.getUsertype());	
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.OK);
	}
}
