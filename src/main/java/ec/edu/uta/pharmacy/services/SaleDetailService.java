package ec.edu.uta.pharmacy.services;

import ec.edu.uta.pharmacy.entities.SaleDetail;
import ec.edu.uta.pharmacy.repositories.SaleDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Pageable page = PageRequest.of(0, count);
        return repository.findAll(page).getContent();
    }
}
