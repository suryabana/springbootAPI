package com.spring.viewModel;

import org.springframework.lang.Nullable;

import com.spring.model.Patient;
import com.spring.model.PatientInformation;

public class PatientAndPatientInformationBody {
	
	private Patient patient;
	private PatientInformation patientInformation;
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public PatientInformation getPatientInformation() {
		return patientInformation;
	}
	public void setPatientInformation(@Nullable PatientInformation patientInformation) {
		this.patientInformation = patientInformation;
	}
	@Override
	public String toString() {
		return "PatientAndPatientInformationBody [patient=" + patient + ", patientInformation=" + patientInformation
				+ "]";
	}
	public PatientAndPatientInformationBody() {}
	
}
