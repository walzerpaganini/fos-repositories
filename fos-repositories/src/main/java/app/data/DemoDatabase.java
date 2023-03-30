package app.data;

import static java.lang.System.out;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import app.data.entities.MedicalTest;
import app.data.entities.Patient;
import app.data.repositories.MedicalTestsRepository;
import app.data.repositories.PatientsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class DemoDatabase implements ApplicationRunner {

	/**
	 * Trovandoci all'interno di un bean i repository richiesti verranno iniettati
	 * automaticamente da Spring tramite il costruttore.
	 */

	private PatientsRepository patientsRepo;
	private MedicalTestsRepository medicalTestsRepo;

	/**
	 * Se i repository di Spring Data dovessero risultare troppo scomodi o insufficienti
	 * per effettuare le operazioni più complesse, possiamo aggiungere la dipendenza
	 * dall'EntityManager di JPA, che ci permette di interagire con l'ORM a un livello
	 * di astrazione più basso (es. scrivendo manualmente le query SQL).
	 */
	
	private EntityManager entityManager;
	
	@Autowired
	public DemoDatabase(EntityManager entityManager, PatientsRepository patientsRepo, MedicalTestsRepository medicalTestsRepo) {
		this.entityManager = entityManager;
		this.patientsRepo = patientsRepo;
		this.medicalTestsRepo = medicalTestsRepo;
	}
	
	/**
	 * Ricordiamo che l'interfaccia ApplicationRunner fa in modo che Spring
	 * esegua il metodo .run() dopo aver istanziato questo bean.
	 */
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		out.println("\n=== DATABASE SETUP & TEST ===\n");

		createPatientsAndTests();
		out.println();

		findAndUpdateExample();
		out.println();

		searchExample();
		out.println();
		
		deleteExample();
		out.println();
		
		entityManagerExample();
		out.println();
	}
	
	private void createPatientsAndTests() {
		Patient p1 = new Patient("Walter", "Paganini");
		Patient p2 = new Patient("Mario", "Rossi");
		Patient p3 = new Patient("Luigi", "Rossi");
		Patient p4 = new Patient("Peter", "Falk");
				
		List<Patient> patients = List.of(p1, p2, p3, p4);
		
		for(Patient p : patients) {
			out.println("ID of " + p.getFullName() + " before saving: " + p.getId());
			patientsRepo.save(p);
			out.println("ID of " + p.getFullName() + " after saving: " + p.getId());
		}
		
		MedicalTest mt1 = new MedicalTest(p1, "Analisi del sangue");
		MedicalTest mt2 = new MedicalTest(p1, "Analisi delle urine");
		MedicalTest mt3 = new MedicalTest(p2, "Analisi del sangue");

		medicalTestsRepo.saveAll(List.of(mt1, mt2, mt3));
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
		
		out.println("Searching all patients with a blood test...");
		
		for(Patient p : patientsRepo.findAllByMedicalTestsDescriptionContaining("sangue")) {
			out.println("Found: " + p.getFullName() + " (ID: " + p.getId() + ")");
		}
		
		out.println("Searching all tests of patient with ID 1...");
		
		for(MedicalTest mt : medicalTestsRepo.findAllByPatientId(1L)) {
			out.println("Found: " + mt.getDescription());
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
	
	private void entityManagerExample() {

		/**
		 * Esempio di query SQL utilizzando l'EntityManager. Questo sistema "rompe" il layer
		 * di astrazione fornito da JPA, perciò va usato con cautela: eventuali modifiche
		 * alle tabelle e alle colonne del DB potrebbero causare errori nell'esecuzione
		 * delle query, dato che ci riferiamo a loro in maniera esplicita, senza il mapping
		 * fornito dall'ORM. Inoltre, siamo anche più facilmente esposti al rischio di
		 * vulnerabilità (es. SQL Injection) se non prendiamo accorgimenti opportuni.
		 */
		
		String query = "SELECT first_name FROM patients WHERE last_name = :lastName";
		
		String firstName = (String) entityManager
										.createNativeQuery(query)
										.setParameter("lastName", "Paganini")
										.getSingleResult();
		
		/**
		 * Utilizzando il linguaggio JPQL di JPA, abbiamo a disposizione un sistema simile
		 * al linguaggio SQL per scrivere le query, ma invece di fare riferimento alle tabelle
		 * e alle colonne del DB, facciamo riferimento alle entità e alle loro proprietà. In
		 * questo modo, possiamo continuare a sfruttare l'astrazione e il mapping dell'ORM.
		 */
		
		Patient patientEntity = entityManager
						.createQuery("FROM Patient WHERE id = :id", Patient.class)
						.setParameter("id", 1L)
						.getSingleResult();
				
		MedicalTest medicalTestEntity = entityManager
							.createQuery("FROM MedicalTest WHERE patient.lastName = :lastName", MedicalTest.class)
							.setParameter("lastName", "Paganini")
							.setMaxResults(1)
							.getSingleResult();
		
		/**
		 * In caso avessimo bisogno di query particolarmente delicate o dinamiche, possiamo
		 * usare anche il sistema delle cosiddette Criteria Query per descrivere le query
		 * senza usare delle stringhe, bensì utilizzando interfacce e metodi ben definiti.
		 * In questo modo limitiamo la possibilità di commettere errori (es. di sintassi)
		 * nella scrittura manuale delle query.
		 */
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Patient> criteriaQuery = builder.createQuery(Patient.class);
		
		Root<Patient> fromPatient = criteriaQuery.from(Patient.class);
		
		Predicate condition = builder.equal(fromPatient.get("lastName"), "Rossi");
		criteriaQuery.where(condition);
		
		List<Patient> patientEntities = entityManager.createQuery(criteriaQuery).getResultList();
		
		for(Patient p : patientEntities) {
			System.out.println(p.getFullName());
		}
	}
}
