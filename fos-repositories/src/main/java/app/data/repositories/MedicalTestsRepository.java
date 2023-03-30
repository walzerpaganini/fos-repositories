package app.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.data.entities.MedicalTest;

public interface MedicalTestsRepository extends CrudRepository<MedicalTest, Long> {
	
	/**
	 * Similmente alle relazioni @OneToMany, possiamo sfruttare anche le
	 * relazioni @ManyToOne.
	 */
	
	List<MedicalTest> findAllByPatientId(long patientId);
}
