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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository repository;
    private final ClientService clientService;
    private final  MedicationService medicationService;
    private final SaleRepository saleRepository;

    public List<Sale> findAll() {
        return repository.findAll();
    }

    public Sale findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
    }

    @Transactional
    public Sale save(SaleRequest saleRequest) {

        // 1. Pre-cargar en un solo fetch para todos los medicamentos referenciados
        List<Long> ids = saleRequest.getDetails().stream().map(SaleDetailRequest::getMedicationId).toList();
        List<Medication> medications = medicationService.findAllById(ids);

        // 2. Contruir la entidad sales
        Client client =  clientService.findById(saleRequest.getClientId());
        Sale sale = new Sale();
        sale.setClient(client);

        // 3. Crear la lista con los detalles
        List<SaleDetail> details = new ArrayList<>();
        for (SaleDetailRequest saleDetailRequest : saleRequest.getDetails()) {

            Medication med = medications.get(Integer.parseInt(String.valueOf(saleDetailRequest.getMedicationId())));
            // controlar stock, por el momento no le tomo en cuenta
            //if (saleDetailRequest.getQuantity() > med.getStock()) {
            //    throw new BusinessException("Stock insuficiente en el medicamento: " + med.getId());
            //}
            //med.reduceStock(saleDetailRequest.getQuantity());

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setMedication(med);
            detail.setQuantity(saleDetailRequest.getQuantity());
            detail.setUnitPrice(saleDetailRequest.getUnitPrice());
            details.add(detail);
        }

        sale.setSaleDetails(details);

        return repository.save(sale);
    }

}
