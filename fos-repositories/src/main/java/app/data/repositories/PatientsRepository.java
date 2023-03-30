package app.data.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.data.entities.Patient;

/**
 * I repository di Spring Data aggiungono un ulteriore layer di astrazione al di sopra
 * dell'ORM utilizzato. Creando delle interfacce che ereditano dalle interfacce di
 * Spring Data, verranno creati automaticamente dei bean che ne implementano i metodi,
 * i quali ci permetteranno di interagire effettivamente con il DB.
 * 
 * In questo caso ereditiamo dall'interfaccia CrudRepository, che ci fornisce già la firma
 * di alcuni metodi per la gestione delle entità sul DB (CRUD = Create, Read, Update, Delete).
 * Per esigenze più specifiche, esistono anche interfacce di altro tipo (es. Repository,
 * oppure PagingAndSortingRepository).
 * 
 * Possiamo aggiungere ulteriori metodi ai nostri repository: utilizzando un particolare
 * "linguaggio" per scrivere i nomi dei metodi, Spring Data sarà in grado di parsarli per
 * determinare quale tipo di operazioni vogliamo effettuare, e comporrà automaticamente
 * le query corrispondenti quando invocheremo realmente i metodi del repository.
 * 
 * I repository di Spring Data supportano una moltitudine di operazioni e comportamenti.
 * Per sapere 
 * 
 * Notate che non è necessario utilizzare l'annotazione @Component perché Spring sia in
 * grado di iniettare i repository di Spring Data come bean. Se decidiamo di non usare
 * Spring Data, ma implementiamo a mano il pattern repository, possiamo annotare i nostri
 * bean con @Repository (che è semplicemente un alias di @Component, ma più descrittivo).
 */

public interface PatientsRepository extends CrudRepository<Patient, Long> {	
	
	/**
	 *
	 */
	
	List<Patient> findAllByLastName(String lastName);
	List<Patient> findAllByBirthdateAfter(LocalDate date);
	List<Patient> findFirst10ByFirstName(String firstName);
	// ...

	/**
	 * Le stesse convenzioni possono essere utilizzate anche per query che non richiedono
	 * necessariamente di estrarre le entità dal DB. Notate che bisogna usare dei tipi di
	 * ritorno compatibili con l'operazione che intendiamo effettuare.
	 */
	
	boolean existsByFirstNameAndLastName(String firstName, String lastName);
	int countByFirstNameStartingWith(String nameStart);
	void deleteAllByFirstName(String firstName);
	
	/**
	 * I metodi dei repository possono essere usati anche sfruttando le relazioni
	 * @OneToMany indicate sulle entità: è sufficiente fare riferimento al nome
	 * della proprietà corrispondente. Le query prodotte dall'ORM includeranno
	 * automaticamente le eventuali operazioni di JOIN necessarie.
	 */
	
	List<Patient> findAllByMedicalTestsDescriptionContaining(String testDescription);
}
