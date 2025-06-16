package ec.edu.uta.pharmacy.repositories;

import ec.edu.uta.pharmacy.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
