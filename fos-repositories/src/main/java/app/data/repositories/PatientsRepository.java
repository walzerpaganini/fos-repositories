package app.data.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.data.entities.Patient;

public interface PatientsRepository extends CrudRepository<Patient, Long> {	
	List<Patient> findAllByLastName(String lastName);
	List<Patient> findAllByBirthdateAfter(LocalDate date);
	List<Patient> findFirst10ByFirstName(String firstName);
	// ...

	boolean existsByFirstNameAndLastName(String firstName, String lastName);
	// ...
	
	int countByFirstNameStartingWith(String nameStart);
	// ...
}
