package ec.edu.uta.pharmacy.repositories;

import ec.edu.uta.pharmacy.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
