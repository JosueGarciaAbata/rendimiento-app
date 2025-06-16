package ec.edu.uta.pharmacy.controllers;

import ec.edu.uta.pharmacy.dtos.SaleRequest;
import ec.edu.uta.pharmacy.entities.Sale;
import ec.edu.uta.pharmacy.services.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@AllArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/")
    public List<Sale> findAll() {
        return saleService.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Sale> save(@RequestBody SaleRequest saleRequest) {
        return ResponseEntity.ok(saleService.save(saleRequest));
    }

}
