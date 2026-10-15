package com.consultapet.petcare.client.application.dto;

public record ClienteRequestDTO(
        String nome,
        String telefone,
        String endereco
) {
}
