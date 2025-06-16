package ec.edu.uta.pharmacy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaleRequest {

    @JsonProperty("client_id") // el json usara esta clave para poblar clientId.
    private Long clientId;

    // jackson ya sabe que debe ser de la forma: details [ {sale_id, medication_id, quantity, unit_pri}, {}, {}, ... ]
    private List<SaleDetailRequest> details;

}
