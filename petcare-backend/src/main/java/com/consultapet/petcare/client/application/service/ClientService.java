
package com.consultapet.petcare.client.application.service;

import com.consultapet.petcare.client.application.dto.ClienteRequestDTO;
import com.consultapet.petcare.client.application.dto.ClienteResponseDTO;
import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPortOut;
import com.consultapet.petcare.client.domain.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClienteRepositoryPortOut repositoryPortOut;

    public ClientService(ClienteRepositoryPortOut repositoryPortOut) {
        this.repositoryPortOut = repositoryPortOut;
    }

    public ClienteResponseDTO criar(ClienteRequestDTO request) {
        Cliente cliente = clienteNormalizado(request);
        Cliente clienteSalvo = salvar(cliente);

        return ClienteResponseDTO.fromEntity(clienteSalvo);
    }

    public ClienteResponseDTO editar(Long id, ClienteRequestDTO dadosAtualizados) {
        Cliente normalizado = clienteNormalizado(dadosAtualizados);

        Cliente clienteExistente = repositoryPortOut.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        clienteExistente.setNome(normalizado.getNome());
        clienteExistente.setTelefone(normalizado.getTelefone());
        clienteExistente.setEndereco(normalizado.getEndereco());

        Cliente clienteSalvo = salvar(clienteExistente);

        return ClienteResponseDTO.fromEntity(clienteSalvo);
    }

    public List<ClienteResponseDTO> listar() {
        return repositoryPortOut.listar()
                .stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    public void excluir(Long id) {
        repositoryPortOut.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        repositoryPortOut.excluir(id);
    }

    private Cliente clienteNormalizado(ClienteRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Os dados do cliente são obrigatórios.");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }

        if (dto.telefone() == null || dto.telefone().isBlank()) {
            throw new IllegalArgumentException("Telefone do cliente é obrigatório.");
        }

        String telefone = dto.telefone().replaceAll("\\D", "");

        if (telefone.length() != 10 && telefone.length() != 11) {
            throw new IllegalArgumentException("O telefone deve conter 10 ou 11 dígitos");
        }

        Cliente cliente = new Cliente();

        cliente.setNome(dto.nome().strip().replaceAll("\\s+", " "));
        cliente.setTelefone(telefone);
        cliente.setEndereco(dto.endereco() == null
                ? null
                : dto.endereco().strip().replaceAll("\\s+", " "));

        return cliente;
    }

    private Cliente salvar(Cliente cliente) {
        return repositoryPortOut.salvar(cliente);
    }
}
