package com.consultapet.petcare.client.application.exception.handler;

import com.consultapet.petcare.client.application.dto.ErrorResponseDTO;
import com.consultapet.petcare.client.application.exception.ClienteNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Diz ao Spring para monitorar todos os Controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> tratarClienteNaoEncontrado(ClienteNaoEncontradoException ex) {

        ErrorResponseDTO erro = new ErrorResponseDTO(
                ex.getMessage(), // Pega a mensagem "Cliente não encontrado" que você escreveu no Service
                HttpStatus.NOT_FOUND.value() // 404
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> tratarArgumentoInvalido(IllegalArgumentException ex) {

        ErrorResponseDTO erro = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value() // 400
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
