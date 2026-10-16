package com.consultapet.petcare.client.application.service;

import com.consultapet.petcare.client.application.dto.ClienteRequestDTO;
import com.consultapet.petcare.client.application.dto.ClienteResponseDTO;
import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPortOut;
import com.consultapet.petcare.client.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClienteServiceTest {

    private final ClienteRepositoryPortOut repositoryPortOut = mock(ClienteRepositoryPortOut.class);
    private final ClienteService clienteService = new ClienteService(repositoryPortOut);

    @Test
    void deveRejeitarClienteSemNome() {
        ClienteRequestDTO dto = new ClienteRequestDTO("   ", "91999999999", "Centro");

        assertThrows(IllegalArgumentException.class, () -> clienteService.criar(dto));

        verify(repositoryPortOut, never()).salvar(any(Cliente.class));
    }

    @Test
    void deveRejeitarTelefoneVazio() {
        ClienteRequestDTO dto = new ClienteRequestDTO("João", " ", "Centro");

        assertThrows(IllegalArgumentException.class, () -> clienteService.criar(dto));

        verify(repositoryPortOut, never()).salvar(any(Cliente.class));
    }

    @Test
    void deveRejeitarTelefoneComQuantidadeInvalidaDeDigitos() {
        ClienteRequestDTO dto = new ClienteRequestDTO("João", "-(91)-9984+45^15909103", "Centro");

        assertThrows(IllegalArgumentException.class, () -> clienteService.criar(dto));

        verify(repositoryPortOut, never()).salvar(any(Cliente.class));
    }

    @Test
    void deveRejeitarDtoNulo() {
        assertThrows(IllegalArgumentException.class, () -> clienteService.criar(null));
        assertThrows(IllegalArgumentException.class, () -> clienteService.editar(1L, null));

        verify(repositoryPortOut, never()).salvar(any(Cliente.class));
    }

    @Test
    void deveNormalizarDadosAntesDeSalvar() {
        ClienteRequestDTO dto = new ClienteRequestDTO(
                "  João   Ramos  ", "-(91)-986+66^1430", "  Rua   das  Flores  ");
        when(repositoryPortOut.salvar(any(Cliente.class)))
                .thenReturn(cliente(1L, "João Ramos", "91986661430", "Rua das Flores"));

        clienteService.criar(dto);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(repositoryPortOut).salvar(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("João Ramos", captor.getValue().getNome());
        assertEquals("91986661430", captor.getValue().getTelefone());
        assertEquals("Rua das Flores", captor.getValue().getEndereco());
    }

    @Test
    void deveChamarRepositorioAoSalvarCliente() {
        ClienteRequestDTO dto = new ClienteRequestDTO("João", "9198445159", "Centro");
        when(repositoryPortOut.salvar(any(Cliente.class)))
                .thenReturn(cliente(1L, "João", "9198445159", "Centro"));

        clienteService.criar(dto);

        verify(repositoryPortOut).salvar(any(Cliente.class));
    }

    @Test
    void deveRetornarClientePersistidoAoSalvar() {
        ClienteRequestDTO dto = new ClienteRequestDTO("João", "(91) 99999-9999", null);
        when(repositoryPortOut.salvar(any(Cliente.class)))
                .thenReturn(cliente(5L, "João Salvo", "91999999999", null));

        ClienteResponseDTO resultado = clienteService.criar(dto);

        assertEquals(Long.valueOf(5L), resultado.id());
        assertEquals("João Salvo", resultado.nome());
        assertEquals("91999999999", resultado.telefone());
        assertNull(resultado.endereco());
        verify(repositoryPortOut).salvar(any(Cliente.class));
    }

    @Test
    void deveRejeitarEdicaoDeClienteInexistente() {
        ClienteRequestDTO dto = new ClienteRequestDTO("João Ramos", "94999999991", "Centro");
        when(repositoryPortOut.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clienteService.editar(1L, dto));

        verify(repositoryPortOut, never()).salvar(any(Cliente.class));
    }

    @Test
    void deveEditarClienteExistentePreservandoId() {
        Cliente existente = cliente(1L, "João", "91999999999", "Centro");
        ClienteRequestDTO dto = new ClienteRequestDTO("  João   Ramos ", "-(94)-9999+++-99991", "  Rua   Nova ");
        when(repositoryPortOut.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(repositoryPortOut.salvar(existente))
                .thenReturn(cliente(1L, "João Ramos", "94999999991", "Rua Nova"));

        ClienteResponseDTO resultado = clienteService.editar(1L, dto);

        assertEquals(Long.valueOf(1L), existente.getId());
        assertEquals("João Ramos", existente.getNome());
        assertEquals("94999999991", existente.getTelefone());
        assertEquals("Rua Nova", existente.getEndereco());
        assertEquals(Long.valueOf(1L), resultado.id());
        assertEquals("João Ramos", resultado.nome());
        assertEquals("94999999991", resultado.telefone());
        verify(repositoryPortOut).buscarPorId(1L);
        verify(repositoryPortOut).salvar(existente);
    }

    @Test
    void deveExcluirClienteExistente() {
        when(repositoryPortOut.buscarPorId(1L))
                .thenReturn(Optional.of(cliente(1L, "João", "91999999999", "Centro")));

        clienteService.deletar(1L);

        verify(repositoryPortOut).deletar(1L);
    }

    @Test
    void deveRejeitarExclusaoDeClienteInexistente() {
        when(repositoryPortOut.buscarPorId(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> clienteService.deletar(1L));

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(repositoryPortOut, never()).deletar(1L);
    }

    @Test
    void deveListarClientes() {
        when(repositoryPortOut.listar()).thenReturn(List.of(
                cliente(1L, "João", "91999999999", "Centro"),
                cliente(2L, "Maria", "94999999991", null)));

        List<ClienteResponseDTO> resultado = clienteService.listar();

        assertEquals(2, resultado.size());
        assertEquals(Long.valueOf(1L), resultado.get(0).id());
        assertEquals("João", resultado.get(0).nome());
        assertEquals("Maria", resultado.get(1).nome());
        assertNull(resultado.get(1).endereco());
        verify(repositoryPortOut).listar();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemClientes() {
        when(repositoryPortOut.listar()).thenReturn(List.of());

        List<ClienteResponseDTO> resultado = clienteService.listar();

        assertTrue(resultado.isEmpty());
        verify(repositoryPortOut).listar();
    }

    private Cliente cliente(Long id, String nome, String telefone, String endereco) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEndereco(endereco);
        return cliente;
    }
}
