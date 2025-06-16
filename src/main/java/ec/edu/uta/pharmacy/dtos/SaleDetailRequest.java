package ec.edu.uta.pharmacy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SaleDetailRequest {

    @JsonProperty("sale_id")
    private Long saleId;

    @JsonProperty("medication_id")
    private Long medicationId;

    private Integer quantity;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

}
