package com.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.spring.viewModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.constants.ResponseCode;
import com.spring.constants.WebConstants;
import com.spring.response.*;
import com.spring.repository.*;
import com.spring.controller.*;
import com.spring.model.*;
import com.spring.util.*;
import com.spring.service.*;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/receptionist")
@CrossOrigin(origins = WebConstants.ALLOWED_URL)
@Api(value = "API to provide functionaly to the users once they are logged in ",
        description = "This API provides the operations for all the staff in hospital based on their roles", produces = "application/json")
public class ReceptionistController {

	//private static Logger logger = Logger.getLogger(ReceptionistController.class.getName());

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PatientInformationRepository patientInfoRepo;

	@Autowired
	private AddressRepository addrRepo;

	@Autowired
	private DoctorRepository doctorRepo;

	@Autowired
	private PatientRepository patientRepo;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private jwtUtil jwtutil;

	
	@PostMapping("/addPatient")
	public ResponseEntity<patientResp> addPatient(@Valid @RequestBody PatientAndPatientInformationBody patient,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) {
		patientResp resp = new patientResp();
		if (Validator.isPatientEmpty(patient)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				if(patient.getPatientInformation().getAddress()!=null) {
					Address add = addrRepo.save(patient.getPatientInformation().getAddress());
					patient.getPatientInformation().setAddress(add);
				}
				patientRepo.saveAndFlush(patient.getPatient());
				resp.setPatientList(patientRepo.findAll());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.PATEINT_REG);
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
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/updatePatient")
	public ResponseEntity<patientResp> updatePatient(@Valid @RequestBody PatientAndPatientInformationBody patientInfo,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) {
		patientResp resp = new patientResp();
		if (Validator.isPatientEmpty(patientInfo)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				if(patientInfo.getPatientInformation().getAddress()!=null) {
					Address add = addrRepo.save(patientInfo.getPatientInformation().getAddress());
					patientInfo.getPatientInformation().setAddress(add);
				}
				if(patientInfo.getPatientInformation().getDoctor()!=null) {
					Doctor doct = doctorRepo.save(patientInfo.getPatientInformation().getDoctor());
					patientInfo.getPatientInformation().setDoctor(doct);
				}
				patientRepo.saveAndFlush(patientInfo.getPatient());
				resp.setPatientList(patientRepo.findAll());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.PATIENT_UPD);
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
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/deletePatient")
	public ResponseEntity<patientResp> deletePatient(@RequestBody PatientFilterBody body,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) throws IOException {
		patientResp resp = new patientResp();
		if (Validator.isInValidPatientFilterBody(body)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				Patient result = new Patient();
				if(body.getPatientId()>0) {
					result = patientRepo.getById(body.getPatientId());
					patientRepo.deleteByPatientId(body.getPatientId());
					patientInfoRepo.deleteByPatient(result);
				}
				else if(body.getEmail()!=null || body.getEmail().trim()!="") {
					PatientInformation patientInformation =patientInfoRepo.findByEmail(body.getEmail());
					patientInfoRepo.deleteByEmail(body.getEmail());
					patientRepo.delete(patientInformation.getPatient());
				}
				else if((body.getFirstName() !=null || body.getFirstName().trim()!="") &&
						(body.getLastName() !=null || body.getLastName().trim()!="")) {
					List<Patient> patient = (patientRepo.findByFirstNameAndLastName(body.getFirstName(),body.getLastName()));
					patientInfoRepo.deleteByPatient(patient.get(0));
					patientRepo.deleteByFirstNameAndLastName(body.getFirstName(), body.getLastName());
					
				}
				else if(body.getMobileNumber()!=null || body.getMobileNumber()!="") {
					patientRepo.deleteByMobileNumber(body.getMobileNumber());
				}
				resp.setPatientList(patientRepo.findAll());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.PATIENT_DEL);
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
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/getPatientDetails")
	public ResponseEntity<getAllPatientsResponse> getPatientDetails(@RequestBody PatientFilterBody body,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) throws IOException {
		getAllPatientsResponse resp = new getAllPatientsResponse();
		if (Validator.isInValidPatientFilterBody(body)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				List<PatientAndPatientInformationBody> result = new ArrayList<PatientAndPatientInformationBody>();
				Patient patient = new Patient();
				PatientInformation pinfo = new PatientInformation();
				PatientAndPatientInformationBody presult = new PatientAndPatientInformationBody();
				if(body.getPatientId()>0) {
					patient = patientRepo.getById(body.getPatientId());
					pinfo = patientInfoRepo.findByPatient(patient);
					presult.setPatient(patient);
					presult.setPatientInformation(pinfo);
					result.add(presult);
					
				}
				else if(body.getEmail()!=null || body.getEmail().trim()!="") {
					pinfo =patientInfoRepo.findByEmail(body.getEmail());
					patient = patientRepo.findByPatientId(pinfo.getPatient().getPatientId());
					presult.setPatient(patient);
					presult.setPatientInformation(pinfo);
					result.add(presult);
				}
				else if(body.getMobileNumber()!=null || body.getMobileNumber()!="") {
					patient = patientRepo.findByMobileNumber(body.getMobileNumber());
					pinfo = patientInfoRepo.findByPatient(patient);
					presult.setPatient(patient);
					presult.setPatientInformation(pinfo);
					result.add(presult);
					
				}
				resp.setPatientInfoBody(result);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.PATIENT_DEL);
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
	@GetMapping("/getPatientsDetails")
	public ResponseEntity<patientResp> getPatients(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		patientResp resp = new patientResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				resp.setPatientList(patientRepo.findAll());
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
		return new ResponseEntity<patientResp>(resp, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getPatientsInformation")
	public ResponseEntity<patientInfoResponse> getPatientsInformation(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			 @PathVariable long id)
			throws IOException {
		patientInfoResponse resp = new patientInfoResponse();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				Patient result = patientRepo.getById(id);
				resp.setPatientInfoList(patientInfoRepo.findByPatient(result));
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
		return new ResponseEntity<patientInfoResponse>(resp, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/getAllPatients")
	public ResponseEntity<getAllPatientsResponse> getPatientsInformation(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		getAllPatientsResponse resp = new getAllPatientsResponse();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN,WebConstants.USER_RECEPTIONIST) != null) {
			try {
				List<PatientAndPatientInformationBody> patientResultList = null;
				List<Patient> patients = patientRepo.findAll();
				patients.forEach(patient->{
					PatientAndPatientInformationBody patientInformationBody = new PatientAndPatientInformationBody();
					patientInformationBody.setPatient(patient);
					patientInformationBody.setPatientInformation(patientInfoRepo.findByPatient(patient));
					patientResultList.add(patientInformationBody);
					
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
