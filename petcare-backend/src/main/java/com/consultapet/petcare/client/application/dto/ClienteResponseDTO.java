package com.consultapet.petcare.client.application.dto;

import com.consultapet.petcare.client.domain.model.Cliente;
import com.consultapet.petcare.pet.domain.model.Pet;

import java.util.List;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String telefone,
        String endereco,
        List<Pet> petsCliente
) {

    public static ClienteResponseDTO fromEntity(Cliente cliente) {

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getPetsCliente()
        );
    }
}
