package ec.edu.uta.pharmacy.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
        name = "sale_details",
        indexes = {
                //@Index(name = "idx_sale_id", columnList = "sale_id"),
                //@Index(name = "idx_medication_id", columnList = "medication_id")
        }
)
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    @JsonBackReference
    private Sale sale;

    @ManyToOne()
    @JoinColumn(name = "medication_id", referencedColumnName = "id")
    private Medication medication;

    private Integer quantity;
    private BigDecimal unitPrice;

}
