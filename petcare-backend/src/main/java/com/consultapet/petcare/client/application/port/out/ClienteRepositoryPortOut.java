package com.consultapet.petcare.client.application.port.out;

import com.consultapet.petcare.client.domain.model.Cliente;

import java.util.List;
import java.util.Optional;


//Contrato do CRUD do banco de dados
public interface ClienteRepositoryPortOut {

    Cliente salvar(Cliente cliente);

    List<Cliente> listar();

    Optional<Cliente> buscarPorId(Long id);

    void deletar(Long id);
}
