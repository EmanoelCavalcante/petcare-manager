package com.consultapet.petcare.client.infrasctructure.web;

import com.consultapet.petcare.client.application.dto.ClienteRequestDTO;
import com.consultapet.petcare.client.application.dto.ClienteResponseDTO;
import com.consultapet.petcare.client.application.service.ClienteService;
import com.consultapet.petcare.client.infrastructure.web.ClienteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar(){
        ClienteController clienteController = new ClienteController(clienteService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(clienteController)
                .build();
    }

    @Test
    void deveRetornar200AoCriarCliente() throws Exception{

        ClienteResponseDTO resposta = new ClienteResponseDTO(
                1L,
                "João Silva",
                "91999999999",
                "Centro"
        );

        when(clienteService.criar(any(ClienteRequestDTO.class)))
                .thenReturn(resposta);

        mockMvc.perform(post("/api/clientes/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                              "nome": "João Silva",
                              "telefone": "(91) 99999-9999",
                              "endereco": "Centro"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.telefone").value("91999999999"))
                .andExpect(jsonPath("$.endereco").value("Centro"));

        verify(clienteService, times(1))
                .criar(any(ClienteRequestDTO.class));
    }

    @Test
    void deveRetornar200AoListarClientes() throws Exception {
        ClienteResponseDTO cliente1 = new ClienteResponseDTO(
                1L,
                "João Silva",
                "91999999999",
                "Centro"
        );

        ClienteResponseDTO cliente2 = new ClienteResponseDTO(
                2L,
                "Pedro Augusto",
                "91999999987",
                "Telegráfo"
        );


        when(clienteService.listar())
                .thenReturn(List.of(cliente1, cliente2));

        mockMvc.perform(get("/api/clientes/listar")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath())

    }
}
