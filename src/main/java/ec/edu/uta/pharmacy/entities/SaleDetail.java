package ec.edu.uta.pharmacy.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "sale_details")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    private Sale sale;

    @ManyToOne()
    @JoinColumn(name = "medication_id", referencedColumnName = "id")
    private Medication medication;

    private Integer quantity;
    private BigDecimal unitPrice;

}
