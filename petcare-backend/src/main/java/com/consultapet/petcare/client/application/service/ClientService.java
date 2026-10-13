package com.consultapet.petcare.client.application.service;

import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPortOut;
import com.consultapet.petcare.client.domain.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService{

    private final ClienteRepositoryPortOut repositoryPortOut;

    public ClientService(ClienteRepositoryPortOut repositoryPort) {
        this.repositoryPortOut = repositoryPort;
    }

    public Cliente salvar(Cliente cliente){

        return repositoryPortOut.salvar(clienteNormalizado(cliente));
    }

    public Cliente editar(Long id, Cliente dadosAtualizados){
        Cliente clienteExistente = repositoryPortOut.buscarPorId(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Cliente não encontrado")
                );

        clienteNormalizado(dadosAtualizados);

        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setTelefone(dadosAtualizados.getTelefone());
        clienteExistente.setEndereco(dadosAtualizados.getEndereco());

        return repositoryPortOut.salvar(clienteExistente);
    }

    public List<Cliente> listar(){
        return repositoryPortOut.listar();
    }

    public void excluir(Long id){
        repositoryPortOut.buscarPorId(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Cliente não encontrado")
                );
        repositoryPortOut.excluir(id);
    }

    private Cliente clienteNormalizado(Cliente cliente){
        String nome = cliente.getNome();
        String telefone = cliente.getTelefone();
        String endereco = cliente.getEndereco();

        if(nome == null || nome.isBlank()){
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }

        if(telefone == null || telefone.isBlank()){
            throw new IllegalArgumentException("Telefone do cliente é obrigatório.");
        }

        if(telefone.length() != 10 && telefone.length() != 11){
            throw new IllegalArgumentException(
                    "O telefone deve conter 10 ou 11 dígitos"
            );
        }

        if(endereco == null || endereco.isBlank()){
            throw new IllegalArgumentException("Endereço do cliente é obrigatório.");
        }

        nome = nome.trim().replaceAll("\\s+"," ");
        telefone = telefone.replaceAll("\\D","");
        endereco = endereco.trim().replaceAll("\\s+"," ");


        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEndereco(endereco);

        return cliente;
    }
}
