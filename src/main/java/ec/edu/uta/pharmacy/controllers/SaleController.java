package ec.edu.uta.pharmacy.controllers;

import ec.edu.uta.pharmacy.dtos.SaleDetailRequest;
import ec.edu.uta.pharmacy.dtos.SaleRequest;
import ec.edu.uta.pharmacy.entities.Sale;
import ec.edu.uta.pharmacy.entities.SaleDetail;
import ec.edu.uta.pharmacy.services.SaleDetailService;
import ec.edu.uta.pharmacy.services.SaleDetailServiceJdbc;
import ec.edu.uta.pharmacy.services.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/sales")
@AllArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final MeterRegistry meterRegistry;
    private final SaleDetailService saleDetailService;
    private final SaleDetailServiceJdbc saleDetailServiceJdbc;

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

    /**
     * Ejecuta escrituras de saleDetails según los detailCounts indicados.
     * Ejemplo: /generate?detailCounts=1,2,3,4,5,6,...
     * Genera 1 detalle, 2 detalles, 3 detalles, etc. Cada detalle es de una venta.
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateSales(@RequestParam("detailCounts") String detailCounts) {

        // Parsear cadena a lista de enteros
        List<Integer> counts = Arrays.stream(detailCounts.split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .toList();

        // Generar cada venta con el número de detalles indicado
        for (int detalleCount : counts) {
            // Inicializar un cronometro en el registro de metricas
            Timer.Sample sample = Timer.start(meterRegistry);

            SaleRequest req = new SaleRequest();
            req.setClientId(1L);

            // Crear la lista de SaleDetailRequest con detalleCount elementos
            List<SaleDetailRequest> detalles = IntStream.rangeClosed(1, detalleCount)
                    .mapToObj(i -> {
                        SaleDetailRequest d = new SaleDetailRequest();
                        d.setMedicationId((long) 1);
                        d.setQuantity(1);
                        d.setUnitPrice(BigDecimal.valueOf(10.0));
                        return d;
                    })
                    .toList();
            req.setDetails(detalles);

            // Llamada optimizada
            saleService.saveOptimized(req);

            // Parar el cronometro y registrarlo con un tag
            sample.stop(
                    Timer.builder("sale.write.time")
                            .description("Time to write a sale")
                            .tag("detail_count", String.valueOf(detalleCount))
                            .register(meterRegistry)
            );
        }

        return ResponseEntity.ok(
                "Generadas ventas con detailCounts=" + counts
        );
    }

    /**
     * Ejecuta lecturas de saleDetails según los detailCounts indicados.
     * Ejemplo: /generate/read?detailCounts=100,1000,4000
     * hará: readFirstNDetails(100), readFirstNDetails(1000), readFirstNDetails(4000).
     * Utiliza hibernate.
     */
    @GetMapping("/generate")
    public ResponseEntity<String> generateReads(@RequestParam("detailCounts") String detailCounts) {

        // Parsear cadena a lista de enteros
        List<Integer> counts = Arrays.stream(detailCounts.split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .toList();

        // Para cada tamaño de lectura, medir y etiquetar
        for (int detailCount : counts) {
            String tag = String.valueOf(detailCount);
            Timer.Sample sample = Timer.start(meterRegistry);

            // Ejecuta la lectura de N detalles
            List<SaleDetail> detalles = saleDetailService.readFirstNDetails(detailCount);

            // Registrar métrica con el tag correspondiente
            sample.stop(
                    Timer.builder("report.read.time")
                            .description("Time to read sale details")
                            .tag("detail_count", tag)
                            .register(meterRegistry)
            );
        }

        return ResponseEntity.ok(
                "Ejecutadas lecturas con detailCounts=" + counts
        );
    }

    // Igual al de hibernate, pero usando JDBC.
    @GetMapping("/read/jdbc")
    public ResponseEntity<String> readJdbc(
            @RequestParam("detailCounts") String detailCounts) {

            // Parsear cadena a lista de ints
            List<Integer> counts = Arrays.stream(detailCounts.split(","))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .toList();

            // Para cada tamaño de lectura, medir y etiquetar
            for (int detailCount : counts) {
                String tag = String.valueOf(detailCount);
                Timer.Sample sample = Timer.start(meterRegistry);

                // Ejecuta la lectura de N detalles
                List<SaleDetail> detalles = saleDetailServiceJdbc.readFirstNDetailsJdbc(detailCount);

                // Registrar métrica con el tag correspondiente
                sample.stop(
                        Timer.builder("report.read.time")
                                .description("Time to read sale details")
                                .tag("detail_count", tag)
                                .register(meterRegistry)
                );
            }

            return ResponseEntity.ok(
                    "Ejecutadas lecturas con detailCounts=" + counts
            );
    }

}
