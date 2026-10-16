package com.consultapet.petcare.client.infrasctructure.web;

import com.consultapet.petcare.client.application.dto.ClienteRequestDTO;
import com.consultapet.petcare.client.application.dto.ClienteResponseDTO;
import com.consultapet.petcare.client.application.exception.ClienteNaoEncontradoException;
import com.consultapet.petcare.client.application.exception.handler.GlobalExceptionHandler;
import com.consultapet.petcare.client.application.service.ClienteService;
import com.consultapet.petcare.client.infrastructure.web.ClienteController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void configurar(){
        ClienteController clienteController = new ClienteController(clienteService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Pedro Augusto"))
                .andExpect(jsonPath("$[0].telefone").value("91999999999"))
                .andExpect(jsonPath("$[1].telefone").value("91999999987"))
                .andExpect(jsonPath("$[0].endereco").value("Centro"))
                .andExpect(jsonPath("$[1].endereco").value("Telegráfo"));

        verify(clienteService, times(1))
                .listar();
    }

    @Test
    void deveRetornarListaVaziaAoRetornarClientes() throws Exception{
        when(clienteService.listar())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/clientes/listar")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(clienteService, times(1))
                .listar();
    }

    @Test
    void deveRetornar200AoEditarCliente() throws Exception{
        // 1. DTO com os novos dados que o usuário enviou na requisição (Request)
        ClienteRequestDTO dadosNovos = new ClienteRequestDTO(
                "João Alterado",
                "91888888888",
                "Centro");

        // 2. DTO que a sua Service deve retornar após salvar no banco (Response)
        ClienteResponseDTO clienteAtualizado = new ClienteResponseDTO(
                1L,
                "João Alterado",
                "91888888888",
                "Centro");


        when(clienteService.editar(eq(1L), any(ClienteRequestDTO.class)))
                .thenReturn(clienteAtualizado);

        mockMvc.perform(put("/api/clientes/editar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosNovos))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk()) // Espera HTTP 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Alterado"))
                .andExpect(jsonPath("$.telefone").value("91888888888"));

        verify(clienteService, times(1)).editar(eq(1L), any(ClienteRequestDTO.class));

    }

    @Test
    void deveRetornar404AoEditarClienteInexistente() throws Exception {
        ClienteRequestDTO dadosNovos = new ClienteRequestDTO(
                "Nome Qualquer",
                "91999999999",
                "Centro"
        );

        when(clienteService.editar(eq(1L), any(ClienteRequestDTO.class)))
                .thenThrow(new ClienteNaoEncontradoException("Cliente não encontrado"));

        // 3. Executa a requisição PUT simulando o erro
        mockMvc.perform(put("/api/clientes/editar/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosNovos)) // Envia o JSON
                        .accept(MediaType.APPLICATION_JSON))

                // 4. VALIDAÇÕES: O seu GlobalExceptionHandler vai capturar o erro e transformar em 404
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Cliente não encontrado"));

        // 5. Verifica se o método do service foi realmente acionado
        verify(clienteService, times(1)).editar(eq(1L), any(ClienteRequestDTO.class));
    }

    @Test
    void deveRetornar204AoExcluirCliente() throws Exception{

        ClienteResponseDTO cliente = new ClienteResponseDTO(
                1L,
                "João Silva",
                "91999999999",
                "Centro"
        );

        mockMvc.perform(delete("/api/clientes/deletar/{id}", cliente.id())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(clienteService, times(1)).deletar(1L);
    }

    @Test
    void deveRetornar404AoTentarExcluirClienteInexistente() throws Exception{
        ClienteResponseDTO cliente = new ClienteResponseDTO(
                1L,
                "João Silva",
                "91999999999",
                "Centro"
        );

        doThrow(new ClienteNaoEncontradoException("Cliente não encontrado"))
                .when(clienteService).deletar(2L);
        mockMvc.perform(delete("/api/clientes/deletar/{id}", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Cliente não encontrado"));

        verify(clienteService, times(1)).deletar(2L);
    }
}
