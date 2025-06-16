package ec.edu.uta.pharmacy.services;

import ec.edu.uta.pharmacy.entities.Client;
import ec.edu.uta.pharmacy.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    public List<Client> findAll() {
        return repository.findAll();
    }

    public Client findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
    }

    public Client save(Client client) {
        return repository.save(client);
    }


}
