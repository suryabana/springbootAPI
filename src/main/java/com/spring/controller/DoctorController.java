package com.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.spring.model.PatientInformation;
import com.spring.model.Tests;
import com.spring.model.User;
import com.spring.repository.DoctorRepository;
import com.spring.repository.LaboratoryRepository;
import com.spring.repository.PatientInformationRepository;
import com.spring.repository.PatientRepository;
import com.spring.repository.TestsRepository;
import com.spring.repository.UserRepository;
import com.spring.response.doctorResp;
import com.spring.response.getAllPatientsResponse;
import com.spring.response.labResp;
import com.spring.response.patientInfoResponse;
import com.spring.response.patientResp;
import com.spring.response.prodResp;
import com.spring.response.serverResp;
import com.spring.response.testResp;
import com.spring.util.Validator;
import com.spring.util.jwtUtil;
import com.spring.viewModel.PatientAndPatientInformationBody;
import com.spring.viewModel.PatientPrescription;
import com.spring.viewModel.PatientTests;

import io.swagger.annotations.Api;


@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("api/doctor")
@Api(value = "API to Maintain the Hospital Data by different inputs of Data",
description = "This API provides the capability to Add , Delete or modify the hospital Data", produces = "application/json")
public class DoctorController {
	
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
	private PatientInformationRepository patientInfoRepo;
	
	@Autowired
	private jwtUtil jwtutil;
	
	
	@GetMapping("/{doctorId}/patients")
	public ResponseEntity<patientResp> getPatients(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			 @PathVariable String doctorId)
			throws IOException {
		patientResp resp = new patientResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN, WebConstants.USER_DOCTOR) != null) {
			try {
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.PATIENT_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setPatientList(patientRepo.findAll());
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{doctorId}/prescription")
	public ResponseEntity<patientResp> savePrescription(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			 @PathVariable String doctorId, @RequestBody PatientPrescription patientPrescription)
			throws IOException {
		patientResp resp = new patientResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_DOCTOR) != null) {
			if(!Validator.isEmptyPrescription(patientPrescription)) {
					try {
							Patient patient =patientRepo.getById(patientPrescription.getPatientId());
							PatientInformation patientInfo = patientInfoRepo.findByPatient(patient);
							patient.setPatientId(patientPrescription.getPatientId());
							patientInfo.setDoctorPrescription(patientPrescription.getPrescription());
							patientRepo.save(patient);
							patientInfoRepo.save(patientInfo);
							resp.setStatus(ResponseCode.SUCCESS_CODE);
							resp.setMessage(ResponseCode.PATIENT_SUCCESS_MESSAGE);
							resp.setAUTH_TOKEN(AUTH_TOKEN);
							resp.setPatientList(patientRepo.findAll());
					} catch (Exception e) {
						resp.setStatus(ResponseCode.FAILURE_CODE);
						resp.setMessage(e.getMessage());
						resp.setAUTH_TOKEN(AUTH_TOKEN);
					}
				}
			} else {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(ResponseCode.FAILURE_MESSAGE);
			}
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{doctorId}/tests")
	public ResponseEntity<patientResp> saveTests(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			 @PathVariable String doctorId, @RequestBody PatientTests patientTests)
			throws IOException {
		patientResp resp = new patientResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_DOCTOR) != null) {
			if(!Validator.isEmptyPatienttests(patientTests)) {
					try {
							Patient patient =patientRepo.getById(patientTests.getPatientId());
							PatientInformation patientInfo = patientInfoRepo.findByPatient(patient);
							patientInfo.setTests(patientTests.getTests());
							patientRepo.save(patient);
							patientInfoRepo.save(patientInfo);
							resp.setStatus(ResponseCode.SUCCESS_CODE);
							resp.setMessage(ResponseCode.PATIENT_SUCCESS_MESSAGE);
							resp.setAUTH_TOKEN(AUTH_TOKEN);
							resp.setPatientList(patientRepo.findAll());
					} catch (Exception e) {
						resp.setStatus(ResponseCode.FAILURE_CODE);
						resp.setMessage(e.getMessage());
						resp.setAUTH_TOKEN(AUTH_TOKEN);
					}
				}
			} else {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(ResponseCode.FAILURE_MESSAGE);
			}
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getAllPatients")
	public ResponseEntity<getAllPatientsResponse> getPatientsInformation(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		getAllPatientsResponse resp = new getAllPatientsResponse();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_DOCTOR) != null) {
			try {
				ArrayList<PatientAndPatientInformationBody> patientResultList = 
			       new ArrayList<PatientAndPatientInformationBody>();
				List<Patient> patients = patientRepo.findAll();
				patients.forEach(patient->{
					PatientAndPatientInformationBody patientInformationBody = new PatientAndPatientInformationBody();
					if(patient!=null) {
					patientInformationBody.setPatient(patient);
					try {
						patientInformationBody.setPatientInformation(patientInfoRepo.findByPatient(patient));
					}catch(Exception e) {
						e.printStackTrace();
						patientInformationBody.setPatientInformation(new PatientInformation());
					}
					if(patientInformationBody!=null) patientResultList.add(patientInformationBody);
					}
					
				});
				resp.setPatientInfoBody(patientResultList);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.LIST_SUCCESS_MESSAGE);
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
		return new ResponseEntity<getAllPatientsResponse>(resp, HttpStatus.ACCEPTED);
	}
	
}
