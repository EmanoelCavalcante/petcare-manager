package com.consultapet.petcare.client.infrastructure.persistence;

import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPortOut;
import com.consultapet.petcare.client.domain.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepositoryPortOut {

    private final ClienteRepository repository;

    public ClienteRepositoryAdapter(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente salvar(Cliente cliente){
       return repository.save(cliente);
    }

    @Override
    public List<Cliente> listar(){
        return repository.findAll();
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id){
       return repository.findById(id);
    }

    @Override
    public void deletar(Long id){
        repository.deleteById(id);
    }
}
