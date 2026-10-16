package com.consultapet.petcare.client.application.dto;

public record ErrorResponseDTO(
        String mensagem,
        int status
) {
}
