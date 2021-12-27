package com.spring.controller;

import java.io.IOException;
import java.util.HashMap;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.spring.constants.ResponseCode;
import com.spring.constants.WebConstants;
import com.spring.model.Doctor;
import com.spring.model.Laboratory;
import com.spring.model.Tests;
import com.spring.model.User;
import com.spring.repository.DoctorRepository;
import com.spring.repository.LaboratoryRepository;
import com.spring.repository.PatientRepository;
import com.spring.repository.TestsRepository;
import com.spring.repository.UserRepository;
import com.spring.response.doctorResp;
import com.spring.response.labResp;
import com.spring.response.serverResp;
import com.spring.response.testResp;
import com.spring.util.Validator;
import com.spring.util.jwtUtil;

import io.swagger.annotations.Api;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("api/admin")
@Api(value = "API to Maintain the Hospital Data by different inputs of Data",
description = "This API provides the capability to Add , Delete or modify the hospital Data", produces = "application/json")
public class AdminController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DoctorRepository doctorRepo;
	
	@Autowired
	private LaboratoryRepository labRepo;

	@Autowired
	private TestsRepository testRepo;
	
	@Autowired
	private PatientRepository patientRepo;
	
	@Autowired
	private jwtUtil jwtutil;
	
	private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

	@PostMapping("/addDoctor")
	public ResponseEntity<doctorResp> addDoctor(@Valid @RequestBody Doctor doctor,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) {
		doctorResp resp = new doctorResp();
		if (Validator.isDoctorEmpty(doctor)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				//User user = jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_DOCTOR);
				Doctor person = new Doctor();
				person.setName(doctor.getName());
				person.setGender(doctor.getGender());
				person.setAge(doctor.getAge());
				person.setSpecilization(doctor.getSpecilization());
				person.setCostPerAppointment(doctor.getCostPerAppointment());
				person.setEmail(doctor.getEmail());
				person.setPhoneNumber(doctor.getPhoneNumber());
				User user = new User();
				user.setAge(doctor.getAge());
				user.setEmail(doctor.getEmail());
				user.setPassword("Default123");
				user.setUsername(doctor.getName()+""+doctor.getAge());
				user.setUsertype(WebConstants.USER_ADMIN_ROLE);
				user.setAge(doctor.getAge());
				userRepo.saveAndFlush(user);
				Doctor doct = doctorRepo.saveAndFlush(person);
				resp.setDoctorlist(doctorRepo.findAll());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DOCT_REG);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<doctorResp>(resp, HttpStatus.OK);
	}
	@PutMapping("/updateDoctor")
	public ResponseEntity<serverResp> updateDoctor(
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@Valid @RequestBody Doctor doctor) throws IOException {

		serverResp resp = new serverResp();
		if (Validator.isDoctorEmpty(doctor)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				Doctor doc = doctorRepo.findByDoctorId(doctor.getDoctorId());
				doc.setAge(doctor.getAge());
				doc.setGender(doctor.getGender());
				doc.setName(doctor.getName());
				doc.setSpecilization(doctor.getSpecilization());
				doc.setCostPerAppointment(doctor.getCostPerAppointment());
				doc.setEmail(doctor.getEmail());
				doc.setPhoneNumber(doctor.getPhoneNumber());
				doctorRepo.save(doc);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.UPD_DOCTOR_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.toString());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/delDoctor")
	public ResponseEntity<doctorResp> deleteDoctor(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestParam(name = WebConstants.DOCTOR_ID) int doctorId) throws IOException {
		doctorResp resp =  new doctorResp();
		if (Validator.isInValidNumber(doctorId)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE); 
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				doctorRepo.deleteByDoctorId(doctorId);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DEL_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setDoctorlist(doctorRepo.findAll());
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.toString());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<doctorResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getDoctors")
	public ResponseEntity<doctorResp> getDoctors(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		doctorResp resp = new doctorResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DOCTOR_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setDoctorlist(doctorRepo.findAll());
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<doctorResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/addLaboratory")
	public ResponseEntity<labResp> addLaboratory(@RequestBody Laboratory laboratory,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) throws IOException {
		labResp resp = new labResp();
		if(Validator.isLaboratoryEmpty(laboratory)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		}
		else if(!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				Laboratory lab = labRepo.saveAndFlush(laboratory);
				resp.setLaboratoryList(labRepo.findAll());
				resp.setMessage(ResponseCode.SUCCESS_CODE);
				resp.setStatus(ResponseCode.SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} 
			catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		}
		return new ResponseEntity<labResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deleteLaboratory")
	public ResponseEntity<doctorResp> deleteLab(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestParam(name = WebConstants.LAB_ID) String labId) throws IOException {
		doctorResp resp =  new doctorResp();
		if (Validator.isStringEmpty((labId))) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				labRepo.deleteByLabId(labId);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DEL_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setDoctorlist(doctorRepo.findAll());
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.toString());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<doctorResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/updateLaboratory")
	public ResponseEntity<serverResp> updateLaboratory(
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@Valid @RequestBody Laboratory laboratory) throws IOException {

		serverResp resp = new serverResp();
		if (Validator.isLaboratoryEmpty(laboratory)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				Laboratory lab = labRepo.findByLabId(laboratory.getLabId());
				lab.setLabName(laboratory.getLabName());
				labRepo.save(lab);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.LAB_UPD);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.toString());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getAllLabaratories")
	public ResponseEntity<labResp> getLaboratories(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		labResp resp = new labResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.LAB_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setLaboratoryList(labRepo.findAll());
				patientRepo.findAll().forEach((e)->System.out.println("Value"+e));
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<labResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/addTests")
	public ResponseEntity<testResp> addTest(@Valid @RequestBody Tests test,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) throws IOException {
		testResp resp = new testResp();
		if(Validator.isTestEmpty(test)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		}
		else if(!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				Tests result = testRepo.saveAndFlush(test);
				resp.setTestList(testRepo.findAll());
				resp.setMessage(ResponseCode.SUCCESS_CODE);
				resp.setStatus(ResponseCode.SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} 
			catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		}
		return new ResponseEntity<testResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/updateTest")
	public ResponseEntity<testResp> updateTest(
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@Valid @RequestBody Tests test) throws IOException {
		
		testResp resp = new testResp();
		if (Validator.isTestEmpty(test)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				Tests result = testRepo.findByTestId(test.getTestId());
				result.setCost(test.getCost());
				result.setTestName(test.getTestName());
				result.setLab(test.getLab());
				testRepo.save(result);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.UPD_DOCTOR_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.toString());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<testResp>(resp, HttpStatus.ACCEPTED);
	}
	
	
	@GetMapping("/getAllTests")
	public ResponseEntity<testResp> getTests(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		testResp resp = new testResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				resp.setTestList(testRepo.findAll());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.TESTS_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<testResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deleteTest")
	public ResponseEntity<testResp> deleteTest(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestParam(name = WebConstants.TEST_ID) long testId) throws IOException {
		testResp resp =  new testResp();
		if (Validator.isInValidNumber((testId))) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null) {
			try {
				testRepo.deleteByTestId(testId);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DEL_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setTestList(testRepo.findAll());
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.toString());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<testResp>(resp, HttpStatus.ACCEPTED);
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
		User loggedUser = userRepo.findByEmailAndPasswordAndUsertype(email, password, WebConstants.USER_ADMIN_ROLE);
		serverResp resp = new serverResp();
		if (loggedUser != null) {
			String jwtToken = jwtutil.createToken(email, password, WebConstants.USER_ADMIN_ROLE);
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
	
	@PostMapping("/registerStaffRole")
	public ResponseEntity<serverResp> registerStaff(@Valid @RequestBody User user,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) {
		serverResp resp = new serverResp();
		if (Validator.isUserEmpty(user)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if(!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_ADMIN_ROLE) != null){
			try {
					User result = userRepo.saveAndFlush(user);
					resp.setStatus(ResponseCode.SUCCESS_CODE);
					resp.setMessage(ResponseCode.SUCCESS_MESSAGE);
					resp.setAUTH_TOKEN(AUTH_TOKEN);
					resp.setEmail(null);
					resp.setUserName(null);
					resp.setUserType(WebConstants.USER_ADMIN_ROLE);		
			} catch (Exception e) {
					resp.setStatus(ResponseCode.FAILURE_CODE);
					resp.setMessage(e.getMessage());
			}
		}
		else {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		}
		
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}

}
