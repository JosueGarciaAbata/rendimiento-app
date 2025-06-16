package ec.edu.uta.pharmacy.services;

import ec.edu.uta.pharmacy.entities.Client;
import ec.edu.uta.pharmacy.entities.Medication;
import ec.edu.uta.pharmacy.repositories.ClientRepository;
import ec.edu.uta.pharmacy.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository repository;

    public List<Medication> findAll() {
        return repository.findAll();
    }

    public Medication findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Medication Not Found"));
    }

    public Medication save(Medication medication) {
        return repository.save(medication);
    }
}
