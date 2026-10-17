package com.consultapet.petcare.pet.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PetRequestDTO(
        Long clienteId,
        String nomePet,
        String raca,
        String especie,
        BigDecimal peso,
        Integer idadeEstimada,
        LocalDate dataEstimativa
) {
}
