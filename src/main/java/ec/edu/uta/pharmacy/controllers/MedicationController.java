package ec.edu.uta.pharmacy.controllers;

import ec.edu.uta.pharmacy.entities.Medication;
import ec.edu.uta.pharmacy.services.MedicationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
@AllArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping("/")
    public ResponseEntity<List<Medication>> findAll() {
        return ResponseEntity.ok(medicationService.findAll());
    }

    @PostMapping("/")
    public ResponseEntity<Medication> save(@RequestBody Medication medication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicationService.save(medication));
    }

}
