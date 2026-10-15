package com.consultapet.petcare.client.application.dto;

import com.consultapet.petcare.client.domain.model.Cliente;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String telefone,
        String endereco
) {

    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEndereco()
        );
    }
}
