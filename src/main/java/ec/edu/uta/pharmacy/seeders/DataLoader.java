package ec.edu.uta.pharmacy.seeders;

import ec.edu.uta.pharmacy.entities.Client;
import ec.edu.uta.pharmacy.entities.Medication;
import ec.edu.uta.pharmacy.repositories.ClientRepository;
import ec.edu.uta.pharmacy.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final MedicationRepository medicationRepository;
    private final Faker faker = new Faker();  // thread-safe

    @Override
    public void run(String... args) throws Exception {
        //insertClients(100000);
        //insertMedications(10000);
    }

    private void insertClients(int n) {
        var clients = IntStream.rangeClosed(1, n)
                .mapToObj(i ->
                                Client.builder()
                                        .name(faker.name().fullName())
                                        .contact(faker.address().fullAddress())
                                        .build())
                .collect(Collectors.toList());

        clientRepository.saveAll(clients);
    }

    private void insertMedications(int n) {
        var meds = IntStream.rangeClosed(1, n)
                .mapToObj(i -> Medication.builder()
                        .price(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 100)))
                        .stock(faker.number().numberBetween(0, 200))
                        .build())
                .collect(Collectors.toList());

        medicationRepository.saveAll(meds);
    }

}

