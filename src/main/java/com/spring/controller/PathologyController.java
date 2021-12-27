package com.spring.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Any;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.constants.ResponseCode;
import com.spring.constants.WebConstants;
import com.spring.model.Patient;
import com.spring.model.PatientInformation;
import com.spring.model.User;
import com.spring.repository.DoctorRepository;
import com.spring.repository.LaboratoryRepository;
import com.spring.repository.PatientInformationRepository;
import com.spring.repository.PatientRepository;
import com.spring.repository.TestsRepository;
import com.spring.repository.UserRepository;
import com.spring.response.radiologistGetResp;
import com.spring.response.serverResp;
import com.spring.response.testPostResponse;
import com.spring.util.Validator;
import com.spring.util.jwtUtil;
import com.spring.viewModel.GetTestPatientList;
import com.spring.viewModel.PatientTests;
import com.spring.viewModel.TestsPostRequest;

import io.swagger.annotations.Api;

@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@RestController
@RequestMapping("api/pathologist")
@Api(value = "API to Maintain the Hospital Data by different inputs of Data",
description = "This API provides the capability to Add , Delete or modify the hospital Data", produces = "application/json")
public class PathologyController {
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DoctorRepository doctorRepo;
	
	@Autowired
	private LaboratoryRepository labRepo;

	@Autowired
	private TestsRepository testRepo;
	
	@Autowired
	private PatientInformationRepository patientInfoRepo;
	
	@Autowired
	private PatientRepository patientRepo;
	
	@Autowired
	private jwtUtil jwtutil;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@GetMapping("/{id}/tests")
	public ResponseEntity<radiologistGetResp> getTests(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			 @PathVariable String id)
			throws IOException {
		radiologistGetResp resp = new radiologistGetResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN, WebConstants.USER_PATHOLOGIST) != null && !Validator.isStringEmpty(id)) {
			try {
				Session s = sessionFactory.openSession();
				Transaction tr= s.beginTransaction();
				String sql = "select p.patient_id, p.mobile_number, p.first_name, p.last_name,pt.tests_test_id,t.test_name from patient p join patient_tests pt join tests t join laboratory l"+
		                " "+"where l.lab_id=:lab_id AND t.lab_id=:lab_id AND pt.tests_test_id=t.test_id AND pt.patient_patient_id=p.patient_id";
				NativeQuery<GetTestPatientList> query = s.createSQLQuery(sql);
				query.setParameter("lab_id", id);
				List<GetTestPatientList> results = query.list();
					resp.setStatus(ResponseCode.SUCCESS_CODE);
					resp.setMessage(ResponseCode.TEST_LIST);
					resp.setAUTH_TOKEN(AUTH_TOKEN);
					resp.setTestList(results);
					tr.commit();
					s.close();
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<radiologistGetResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{id}/tests")
	public ResponseEntity<testPostResponse> saveTestReport(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestBody TestsPostRequest scanReport)
			throws IOException {
		testPostResponse resp = new testPostResponse();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_PATHOLOGIST) != null && !Validator.isEmptyTestPostRequest(scanReport)) {
			try {
					Patient patient = patientRepo.findByPatientId(scanReport.getPatientId());
					PatientInformation patientInfo = patientInfoRepo.findByPatient(patient);
					patientInfo.setBills(scanReport.getBills());
					patientInfo.setFiles(scanReport.getScanReports());
					patientRepo.save(patient);
					resp.setStatus(ResponseCode.SUCCESS_CODE);
					resp.setMessage(ResponseCode.SCAN_REPORTS_SAVE);
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
		return new ResponseEntity<testPostResponse>(resp, HttpStatus.ACCEPTED);
	}
}
