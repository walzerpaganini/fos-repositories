package app.data.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Lo standard JPA prevedere di scrivere delle particolari classi che definiscono le
 * entità da mappare alle tabelle del DB. Per farlo, è necessario annotare le classi
 * e i suoi campi (o le sue proprietà) con un insieme di annotazioni che descrivono
 * le caratteristiche delle tabelle, delle colonne, ecc. per permettere il mapping
 * tra le righe del DB e gli oggetti Java corrispondenti.
 *  
 * Attenzione: possono verificarsi problemi se scriviamo alcune annotazioni sui campi
 * della classe e altre sulle proprietà (getter/setter); in particolare, il rischio
 * è che alcune annotazioni vengano ignorate dall'ORM. Dove possibile, è bene scriverle
 * tutte sui campi, oppure tutte sulle proprietà.
 */

@Entity
@Table(name="patients")
public class Patient {
	@Id @GeneratedValue
	private Long id;
	
	private String firstName;
	private String lastName;
	private LocalDate birthdate;
	
	@OneToMany(mappedBy="patient")
	private Set<MedicalTest> medicalTests = new HashSet<>();

	public Patient() {}
	
	public Patient(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

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
	
	public Set<MedicalTest> getMedicalTests() {
		return medicalTests;
	}

	public void setMedicalTests(Set<MedicalTest> medicalTests) {
		this.medicalTests = medicalTests;
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