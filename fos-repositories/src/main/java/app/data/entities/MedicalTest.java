package app.data.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="medical_tests")
public class MedicalTest {
	
	@Id @GeneratedValue
	private Long id;
	private String description;
	private LocalDateTime dateTime;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="patient_id")
	private Patient patient;
	
	public MedicalTest() {}
	
	public MedicalTest(Patient patient, String description) {
		setPatient(patient);
		setDescription(description);
		this.dateTime = LocalDateTime.now();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDateTime getDate() {
		return dateTime;
	}
	
	public void setDate(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
}
