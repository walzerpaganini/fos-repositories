package app.data;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.data.entities.Patient;
import app.data.repositories.PatientsRepository;

import static java.lang.System.out;

@Component
public class DemoDatabase implements ApplicationRunner {

	private PatientsRepository patientsRepo;
	
	@Autowired
	public DemoDatabase(PatientsRepository patientsRepo) {
		this.patientsRepo = patientsRepo;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		out.println("\n=== DATABASE SETUP & TEST ===\n");

		createPatients();
		out.println();

		findAndUpdateExample();
		out.println();

		searchExample();
		out.println();
		
		deleteExample();
		out.println();
	}
	
	private void createPatients() {
		Patient p1 = new Patient("Walter", "Paganini");
		Patient p2 = new Patient("Mario", "Rossi");
		Patient p3 = new Patient("Luigi", "Rossi");
		Patient p4 = new Patient("Peter", "Falk");
				
		for(Patient p : List.of(p1, p2, p3, p4)) {
			out.println("ID of " + p.getFullName() + " before saving: " + p.getId());
			patientsRepo.save(p);
			out.println("ID of " + p.getFullName() + " after saving: " + p.getId());
		}		
	}

	private void findAndUpdateExample() {
		Optional<Patient> optionalPatient = patientsRepo.findById(1L);
		Patient p1 = optionalPatient.get();
		
		out.println("Birthdate of " + p1.getFullName() + " before update: " + p1.getBirthdate());
		p1.setBirthdate(LocalDate.of(1992, 10, 11));
		patientsRepo.save(p1);
		
		p1 = patientsRepo.findById(p1.getId()).get();
		out.println("Birthdate of " + p1.getFullName() + " after update: " + p1.getBirthdate());
	}
	
	private void searchExample() {
		out.println("Searching all patients with last name Rossi...");
		
		for(Patient p : patientsRepo.findAllByLastName("Rossi")) {
			out.println("Found: " + p.getFullName() + " (ID: " + p.getId() + ")");
		}
	}
	
	private void deleteExample() {
		long idToDelete = 4;
		
		out.println("Number of patients on DB before deletion: " + patientsRepo.count());
		patientsRepo.deleteById(idToDelete);
		out.println("Number of patients on DB after deletion: " + patientsRepo.count());
		
		out.println("Searching for the deleted patient...");
		Optional<Patient> optionalPatient = patientsRepo.findById(idToDelete);

		try {
			Patient patient = optionalPatient.orElseThrow();
			out.println("Found: " + patient.getFullName());
			
		} catch(NoSuchElementException e) {
			out.println(e.getMessage());
		}
	}
}
