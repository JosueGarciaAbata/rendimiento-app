package ec.edu.uta.pharmacy.repositories;

import ec.edu.uta.pharmacy.entities.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {
}
