package ec.edu.uta.pharmacy.repositories;

import ec.edu.uta.pharmacy.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
