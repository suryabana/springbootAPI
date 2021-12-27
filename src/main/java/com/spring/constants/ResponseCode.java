package com.spring.constants;

public class ResponseCode {

	// Response
	public static final String SUCCESS_CODE = "200";
	public static final String SUCCESS_MESSAGE = "SUCCESS";

	public static final String FAILURE_CODE = "500";
	public static final String FAILURE_MESSAGE = "ERROR";

	public static final String BAD_REQUEST_CODE = "400";
	public static final String BAD_REQUEST_MESSAGE = "BAD_REQUEST";

	public static final String ADD_SUCCESS_MESSAGE = "ADD_PRO";
	public static final String UPD_SUCCESS_MESSAGE = "UPD_PRO";
	public static final String DEL_SUCCESS_MESSAGE = "DEL_PRO";
	public static final String VIEW_SUCCESS_MESSAGE = "VW_ORD";
	public static final String UPD_DOCTOR_SUCCESS_MESSAGE = "UPDATED_DOCTOR";
	public static final String LIST_SUCCESS_MESSAGE = "LIST_PRO";

	// DOCTOR
	public static final String DOCT_REG = "DOCTOR REGISTERED";
	public static final String DOCTOR_UPD = "DOCTOR_UPDATED";
	public static final String DOCTOR_DEL = "DOCTOR DELETED";
	public static final String DOCTOR_SUCCESS_MESSAGE = "DOCTORS_LIST";
	
	// LABORATORY
	public static final String LAB_REG = "LAB REGISTERED";
	public static final String LAB_UPD = "LAB UPDATED";
	public static final String LAB_DEL = "LAB DELETED";
	public static final String LAB_SUCCESS_MESSAGE = "LABORATORY_LIST";
	
	//TESTS
	public static final String TEST_REG = "TEST REGISTERED";
	public static final String TEST_UPD = "TEST UPDATED";
	public static final String TEST_DEL = "TEST DELETED";
	public static final String TESTS_SUCCESS_MESSAGE = "TESTS_LIST";
	
	//PATIENTS
	public static final String PATEINT_REG = "PATIENT REGISTERED";
	public static final String PATIENT_UPD = "PATIENT UPDATED";
	public static final String PATIENT_DEL = "PATIENT DELETED";
	public static final String PATIENT_SUCCESS_MESSAGE = "PATIENT_LIST";
	
	//TESTSLABS
	public static final String SCAN_REPORTS_SAVE ="SUCCESSFULLY_SAVED_SCAN_REPORTS";
	public static final String TEST_LIST="TESTS_LIST";
	
	// CUSTOMER
	public static final String CUST_REG = "REGISTERED";
	public static final String CUST_ADR_ADD = "ADR_UPD";

	public static final String CART_UPD_MESSAGE_CODE = "CART_UPD";
	public static final String VW_CART_MESSAGE = "LIST_CART";

	public static final String UPD_LAB_MESSAGE = "UPD_CART";

	public static final String DEL_CART_SUCCESS_MESSAGE = "DEL_CART";

	// Order
	public static final String ORD_STATUS_CODE = "PENDING";
	public static final String ORD_SUCCESS_MESSAGE = "PLA_ORD";

	// Missing Information
	public static final String MISSING_FAIL_MSG = "INFO_MISSING";
	public static final String INVALID_EMAIL_FAIL_MSG = "INVALID_EMAIL";
}
