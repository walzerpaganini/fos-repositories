package app.data.entities;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="patients")
public class Patient {
	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate birthdate;

	public Patient() {}
	
	public Patient(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length=100, nullable=false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(length=100, nullable=false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	@Transient
	public Integer getAge() {
		if(birthdate == null) {
			return null;
		}
				
		Period p = Period.between(birthdate, LocalDate.now());
		return p.getYears();
	}
}