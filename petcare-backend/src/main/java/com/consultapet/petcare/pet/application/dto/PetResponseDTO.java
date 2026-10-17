package com.consultapet.petcare.pet.application.dto;

import com.consultapet.petcare.pet.domain.model.Pet;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PetResponseDTO(
        Long id,
        String nomePet,
        Long clienteId,
        String raca,
        String especie,
        BigDecimal peso,
        Integer idadeEstimada,
        LocalDate dataEstimativa
) {
    public static PetResponseDTO fromEntity(Pet pet){
        Long clienteId = (pet.getCliente() != null) ? pet.getCliente().getId() : null;
        return new PetResponseDTO(
                pet.getId(),
                pet.getNomePet(),
                clienteId,
                pet.getRaca(),
                pet.getEspecie(),
                pet.getPeso(),
                pet.getIdadeEstimada(),
                pet.getDataEstimativa()
        );
    }
}
