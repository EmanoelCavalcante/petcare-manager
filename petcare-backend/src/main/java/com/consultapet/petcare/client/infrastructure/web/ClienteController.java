package com.consultapet.petcare.client.infrastructure.web;

import com.consultapet.petcare.client.application.dto.ClienteRequestDTO;
import com.consultapet.petcare.client.application.dto.ClienteResponseDTO;
import com.consultapet.petcare.client.application.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/criar")
    public ResponseEntity<ClienteResponseDTO> criarCliente(
            @RequestBody ClienteRequestDTO request
            ){
        ClienteResponseDTO clienteCriado = clienteService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCriado);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes(){
        return ResponseEntity.ok(clienteService.listar());
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<ClienteResponseDTO> editarCliente(
            @PathVariable Long id,
            @RequestBody ClienteRequestDTO request
    ){
        return ResponseEntity.ok(clienteService.editar(id, request));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCliente(
            @PathVariable Long id
    ){
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
