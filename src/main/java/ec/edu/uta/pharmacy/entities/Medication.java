package ec.edu.uta.pharmacy.entities;

import ec.edu.uta.pharmacy.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private Integer stock;

    public void reduceStock(Integer quantity) {
        this.stock = this.stock - quantity;
    }
}
