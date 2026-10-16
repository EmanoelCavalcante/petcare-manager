package com.consultapet.petcare.client.application.exception;

public class ClienteNaoEncontradoException extends RuntimeException {

    // Construtor padrão que aceita a mensagem customizada
    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
