package ec.edu.uta.pharmacy.services;

import ec.edu.uta.pharmacy.entities.Sale;
import ec.edu.uta.pharmacy.entities.SaleDetail;
import ec.edu.uta.pharmacy.repositories.SaleDetailRepository;
import ec.edu.uta.pharmacy.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SaleDetailService {

    private final SaleDetailRepository repository;

    public List<SaleDetail> findAll() {
        return repository.findAll();
    }

    public SaleDetail findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
    }

    public SaleDetail save(SaleDetail saleDetail) {
        return repository.save(saleDetail);
    }

    public List<SaleDetail> readFirstNDetails(int count) {
        //Pageable page = PageRequest.of(0, count);
         //return repository.findAll(page).getContent();
        return repository.findAll().subList(0, count);
    }
}
