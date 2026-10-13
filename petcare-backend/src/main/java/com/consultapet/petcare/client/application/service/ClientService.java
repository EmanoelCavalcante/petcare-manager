package com.consultapet.petcare.client.application.service;

import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPort;
import com.consultapet.petcare.client.domain.model.Cliente;

import java.util.List;

public class ClientService{

    private final ClienteRepositoryPort repositoryPort;

    public ClientService(ClienteRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public Cliente salvar(Cliente cliente){

        return repositoryPort.salvar(clienteNormalizado(cliente));
    }

    private Cliente clienteNormalizado(Cliente cliente){
        String nome = cliente.getNome();
        String telefone = cliente.getTelefone();

        if(nome == null || !nome.isEmpty()){
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }

        if(telefone == null || !telefone.isEmpty()){
            throw new IllegalArgumentException("Telefone do cliente é obrigatório.");
        }
        nome = nome.trim().replaceAll("\\s+","");
        telefone = telefone.replaceAll("\\D","");

        cliente.setNome(nome);
        cliente.setTelefone(telefone);

        return cliente;
    }
}
