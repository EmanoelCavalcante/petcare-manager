package com.consultapet.petcare.client.application.port.out;

import com.consultapet.petcare.client.domain.model.Cliente;

import java.util.List;


//Contrato do CRUD do banco de dados
public interface ClienteRepositoryPort {

    Cliente salvar(Cliente cliente);

    List<Cliente> listar();

    Cliente editar(Long id);

    void excluir(Long id);
}
