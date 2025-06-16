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
        // formar la venta

        // crear el encabezado de la venta
        Client client = clientService.findById(saleRequest.getClientId()); // ya se lanza la excepcion si no existe el cliente.
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setCreatedAt(LocalDateTime.now());

        // crear los detalles de la venta
        List<SaleDetail> listDetails = new ArrayList<>();
        for (SaleDetailRequest saleDetailRequest : saleRequest.getDetails()) {

            Medication medication = medicationService.findById(saleDetailRequest.getMedicationId());
            Integer quantity = saleDetailRequest.getQuantity();

            if (quantity > medication.getStock()) {
                throw new BusinessException("Stock insuficiente en el medicamento: " + medication.getId());
            }

            medication.reduceStock(quantity);

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setMedication(medication);
            detail.setQuantity(quantity);
            detail.setUnitPrice(saleDetailRequest.getUnitPrice());
            listDetails.add(detail);
        }

        sale.setSaleDetails(listDetails);

        // guardar la venta
        return repository.save(sale);
    }

}
