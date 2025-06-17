package ec.edu.uta.pharmacy.controllers;

import ec.edu.uta.pharmacy.dtos.SaleRequest;
import ec.edu.uta.pharmacy.entities.Sale;
import ec.edu.uta.pharmacy.entities.SaleDetail;
import ec.edu.uta.pharmacy.services.SaleDetailService;
import ec.edu.uta.pharmacy.services.SaleService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.List;

@RestController
    @RequestMapping("/api/v1/sales")
@AllArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final MeterRegistry meterRegistry;
    private final SaleDetailService saleDetailService;

    @GetMapping("/")
    public List<Sale> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/read")
    public ResponseEntity<List<SaleDetail>> readDetails(@RequestParam("count") int count) {

        // el parametro sera el tag
        String detailCount = String.valueOf(count);

        // empezamos el cronometro
        Timer.Sample sample = Timer.start(meterRegistry);

        // ejecutar la lectura
        List<SaleDetail> detalles = saleDetailService.readFirstNDetails(count);

        // parar el cronometro y registrar la metrica con el tag
        sample.stop(
                Timer.builder("report.read.time")
                        .description("Time to read sale deatils")
                        .tag("detail_count", detailCount)
                        .register(meterRegistry)
        );

        return ResponseEntity.ok(detalles);
    }

    @PostMapping("/")
    public ResponseEntity<Sale> save(@RequestBody SaleRequest saleRequest) {
        // capturar los detalles
        String detailCount = String.valueOf(saleRequest.getDetails().size());

        // inicializar un cronometro en el registro de metricas
        Timer.Sample sample = Timer.start(meterRegistry);

        // hacer la logica mia
        Sale saved = saleService.save(saleRequest);

        // parar el cronometro y registrarlo con un tag
        sample.stop(
                Timer.builder("sale.write.time")
                        .description("Time to write a sale")
                        .tag("detail_count", detailCount)
                        .register(meterRegistry)
        );

        return ResponseEntity.ok(saved);
    }

}
