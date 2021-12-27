package com.spring.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.spring.util.StringPrefixedSequenceIdGenerator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name="Laboratory")
@Table(name = "Laboratory")
@AllArgsConstructor
@NoArgsConstructor
public class Laboratory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9177068300756982303L;
	
	@Id
    @Column(nullable=false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laboratory_sequence")
    @GenericGenerator(
             name = "laboratory_sequence",
             strategy = "com.spring.util.StringPrefixedSequenceIdGenerator",
             parameters = {
                       @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                       @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "LAB00"),
                       @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%02d")})
    
	private String labId;
	@NotNull
	@Column(nullable = false)
	private String labName;
	
	public String getLabId() {
		return labId;
	}
	public void setLabId(String labId) {
		this.labId = labId;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	
	@Override
	public String toString() {
		return "Laboratory [labId=" + labId + ", labName=" + labName + "]";
	}
	public Laboratory(String labId, @NotNull String labName) {
		this.labId = labId;
		this.labName = labName;
	}	
	
	public Laboratory() {
		
	}
	
}
