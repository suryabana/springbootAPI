package com.spring.util;

import org.aspectj.weaver.ast.Test;

import com.spring.model.Address;
import com.spring.model.Doctor;
import com.spring.model.Laboratory;
import com.spring.model.Patient;
import com.spring.model.Tests;
import com.spring.model.User;
import com.spring.viewModel.PatientAndPatientInformationBody;
import com.spring.viewModel.PatientFilterBody;
import com.spring.viewModel.PatientPrescription;
import com.spring.viewModel.PatientTests;
import com.spring.viewModel.TestsPostRequest;

public class Validator {

	public static boolean isInValidNumber(long input) {
		if(input == 0 || input<0) {
			return true;
		}
		return false;
	}
	public static boolean isAlphaNumerical(String input) {

		if (input != null && input != "") {
			if (input.matches("[a-zA-Z0-9]*")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNumerical(String input) {
		if (input != null && input != "") {
			if (input.matches("[0-9]*")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValidEmail(String input) {
		if (input != null && input != "") {
			if (input.matches("^[a-zA-Z0-9._]*@[a-zA-Z0-9.-]*$")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isImageFile(String input) {
		if (input != null && input != "") {
			input = input.toLowerCase();
			if (input.endsWith(".png") || input.endsWith(".jpg") || input.endsWith(".jpeg") || input.endsWith(".gif")) {
				return true;
			}
		}
		return false;
	}

	public static String removeSpaces(String input) {
		String filterInput = "";
		if (input != null && input != "") {
			filterInput = input.replace(" ", "");
		}
		return filterInput;
	}

	public static boolean isUserEmpty(User user) {
		if (user.getAge() == 0) {
			return true;
		}
		if (user.getPassword() == null || user.getPassword() == "") {
			return true;
		}
		if (user.getEmail() == null || user.getEmail() == "") {
			return true;
		}
		if (user.getUsername() == null || user.getUsername() == "") {
			return true;
		}
		return false;
	}

	public static boolean isAddressEmpty(Address address) {
		if (address.getAddress() == null || address.getAddress() == "") {
			return true;
		}
		if (address.getCity() == null || address.getCity() == "") {
			return true;
		}
		if (address.getState() == null || address.getState() == "") {
			return true;
		}
		if (address.getCountry() == null || address.getCountry() == "") {
			return true;
		}
		if (address.getPhoneNumber() == null || address.getPhoneNumber() == "") {
			return true;
		}
		if (address.getZipcode() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isPatientEmpty(PatientAndPatientInformationBody patient) {
		if(patient.getPatient().getMobileNumber() == null || patient.getPatient().getMobileNumber() == "") {
			return true;
		}
		else if(patient.getPatient().getDisease() == null || patient.getPatient().getDisease() == "") {
			return true;
		}
		else if(patient.getPatient().getFirstName() == null || patient.getPatient().getFirstName() == "") {
			return true;
		}
		else if(patient.getPatient().getLastName() == null || patient.getPatient().getLastName() == "") {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isDoctorEmpty(Doctor doctor) {

		if (doctor.getName() == null || doctor.getName() == "") {
			return true;
		}
		if (doctor.getAge() == 0) {
			return true;
		}
		if (doctor.getGender() == null || doctor.getGender() == "") {
			return true;
		}
		if (doctor.getSpecilization() == null || doctor.getSpecilization() == "") {
			return true;
		}
		return false;
	}
	
	public static boolean isInValidPatientFilterBody(PatientFilterBody body) {
		if((body.getPatientId() <=0) &&
		    (body.getEmail() == null || body.getEmail() == "") && 
		    ((body.getFirstName() == null || body.getFirstName() == "") &&
		    (body.getLastName() == null || body.getLastName() == "")) &&
		    (body.getMobileNumber() == null || body.getMobileNumber() == "")) {
			return true;
		}
		else return false;
	}
	
	public static boolean isLaboratoryEmpty(Laboratory lab) {
		if(lab.getLabName() == null || lab.getLabName() =="") {
			return true;
		}
		return false;
	}

	public static boolean isTestEmpty(Tests test) {
		if(test.getTestName() == null || test.getTestName() =="") {
			return true;
		}
		return false;
	}
	public static boolean isStringEmpty(String input) {
		if (input == null || input.equals("")) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmptyPrescription(PatientPrescription patientPrescription) {
		if(patientPrescription.getPatientId() <= 0) {
			return true;
		}
		else if(patientPrescription.getPrescription()==null || patientPrescription.getPrescription()== "") {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isEmptyPatienttests(PatientTests patientTests) {
		if(patientTests.getPatientId()<=0) {
			return true;
		}
		else if(patientTests.getTests() == null || patientTests.getTests().size()<=0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isEmptyTestPostRequest(TestsPostRequest testPostRequest) {
		if(testPostRequest.getPatientId()<=0) {
			return true;
		}
		else if(testPostRequest.getPatientMobileNo()=="" || testPostRequest.getPatientMobileNo()==null) {
			return true;
		}
		else if(testPostRequest.getTestId()<=0) {
			return true;
		}
		else if(testPostRequest.getTestName()==null || testPostRequest.getTestName()=="") {
			return true;
		}
		else if(testPostRequest.getScanReports().size()<=0) {
			return true;
		}
		else {
			return false;
		}
	}
}
