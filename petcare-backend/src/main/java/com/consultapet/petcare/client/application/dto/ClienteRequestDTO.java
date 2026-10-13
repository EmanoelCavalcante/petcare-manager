package com.consultapet.petcare.client.application.dto;

import com.consultapet.petcare.pet.domain.model.Pet;

import java.util.List;

public record ClienteRequestDTO(
        String nome,
        String endereco,
        String telefone,
        List<Pet> petsCliente
) {
}
