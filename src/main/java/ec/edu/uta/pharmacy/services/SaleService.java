package ec.edu.uta.pharmacy.services;

import ec.edu.uta.pharmacy.dtos.SaleDetailRequest;
import ec.edu.uta.pharmacy.dtos.SaleRequest;
import ec.edu.uta.pharmacy.entities.Client;
import ec.edu.uta.pharmacy.entities.Medication;
import ec.edu.uta.pharmacy.entities.Sale;
import ec.edu.uta.pharmacy.entities.SaleDetail;
import ec.edu.uta.pharmacy.exceptions.BusinessException;
import ec.edu.uta.pharmacy.repositories.ClientRepository;
import ec.edu.uta.pharmacy.repositories.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository repository;
    private final ClientService clientService;
    private final MedicationService medicationService;

    public List<Sale> findAll() {
        return repository.findAll();
    }

    public Sale findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
    }

    @Transactional
    public Sale save(SaleRequest saleRequest) {
        return saveOptimized(saleRequest);
    }

    @Transactional
    public Sale saveOptimized(SaleRequest saleRequest) {
        // 1. Pre-cargar en una sola consulta todas las entidades Medication
        List<Long> medIds = saleRequest.getDetails().stream()
                .map(SaleDetailRequest::getMedicationId)
                .toList();
        List<Medication> meds = medicationService.findAllById(medIds);
        Map<Long, Medication> medsMap = meds.stream()
                .collect(Collectors.toMap(Medication::getId, m -> m));

        // 2. Construir la entidad Sale
        Sale sale = new Sale();
        sale.setClient(clientService.findById(saleRequest.getClientId()));
        sale.setCreatedAt(LocalDateTime.now());

        // 3. Crear y asociar los detalles de venta
        List<SaleDetail> detalles = saleRequest.getDetails().stream()
                .map(req -> {
                    Medication med = medsMap.get(req.getMedicationId());
                    SaleDetail detail = new SaleDetail();
                    detail.setSale(sale);
                    detail.setMedication(med);
                    detail.setQuantity(req.getQuantity());
                    detail.setUnitPrice(req.getUnitPrice());
                    return detail;
                })
                .toList();
        sale.setSaleDetails(detalles);

        // 4. Guardar todo en batch
        return repository.save(sale);
    }




}
