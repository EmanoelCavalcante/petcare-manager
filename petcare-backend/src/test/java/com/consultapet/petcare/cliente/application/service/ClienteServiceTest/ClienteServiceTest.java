package com.consultapet.petcare.cliente.application.service.ClienteServiceTest;

import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPortOut;
import com.consultapet.petcare.client.application.service.ClientService;
import com.consultapet.petcare.client.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    private final ClienteRepositoryPortOut repositoryPortOut = Mockito.mock(ClienteRepositoryPortOut.class);
    private final ClientService clienteService = new ClientService(repositoryPortOut);
    private static final Logger log = LoggerFactory.getLogger(ClienteServiceTest.class);

    @Test
    void deveRejeitarClienteSemNome() {

        Cliente cliente = new Cliente();

        cliente.setNome("   ");
        cliente.setTelefone("91999999999");
        cliente.setEndereco("Centro");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.salvar(cliente)

        );
        log.info("Exceção esperada: {}", exception.getMessage());
        log.info("Teste aprovado: deveRejeitarClienteSemNome");

    }

    @Test
    void deveRejeitarTelefoneVazio(){

        Cliente cliente = new Cliente();

        cliente.setNome("João");
        cliente.setTelefone(" ");
        cliente.setEndereco("Centro");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.salvar(cliente)
        );
        log.info("Exceção esperada: {}", exception.getMessage());
        log.info("Teste aprovado: deveRejeitarTelefoneVazio");

    }


    @Test
    void deveNormalizarTelefoneComCaracteresEspeciais(){

        Cliente cliente = new Cliente();

        cliente.setNome("João");
        cliente.setTelefone("-(91)-986+66^1430");
        cliente.setEndereco("Centro");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.salvar(cliente)
        );
        log.info("Exceção esperada: {}", exception.getMessage());
        log.info("Teste aprovado: deveNormalizarTelefoneComCaracteresEspeciais");

    }

    @Test
    void deveRejeitarTelefoneComQuantidadeInvalidaDeDigitos(){

        Cliente cliente = new Cliente();

        cliente.setNome("João");
        cliente.setTelefone("-(91)-9984+45^15909103");
        cliente.setEndereco("Centro");

       IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.salvar(cliente)
        );
        log.info("Exceção esperada: {}", exception.getMessage());
        log.info("Teste aprovado: deveRejeitarTelefoneComQuantidadeInvalidaDeDigitos");

    }

    @Test
    void deveChamarRepositorioAoSalvarCliente(){
        Cliente cliente = new Cliente();

        cliente.setNome("João");
        cliente.setTelefone("-(91)-984+45^1590");
        cliente.setEndereco("Centro");

        clienteService.salvar(cliente);

        verify(repositoryPortOut, times(1)).salvar(cliente);
    }

    @Test
    void deveNormalizarTelefoneAntesDeSalvar(){
        String telefone = "91{}9-999+99999";

        Cliente cliente = new Cliente();

        cliente.setNome("Jeferson");
        cliente.setTelefone(telefone);
        cliente.setEndereco("Rua malvino de Sousa");

        clienteService.salvar(cliente);

        verify(repositoryPortOut, times(1)).salvar(cliente);

        assertEquals("91999999999", cliente.getTelefone());

    }

    @Test
    void deveRetornarClienteAoSalvar(){
        Cliente cliente = new Cliente();

        cliente.setNome("João");
        cliente.setTelefone("(91){}9-999+99999");
        cliente.setEndereco("Centro");

        when(repositoryPortOut.salvar(cliente))
                .thenReturn(cliente);

        Cliente resultado = clienteService.salvar(cliente);
        verify(repositoryPortOut, times(1)).salvar(cliente);

        assertEquals("João", resultado.getNome());
        assertEquals("91999999999", resultado.getTelefone());
        assertEquals("Centro", resultado.getEndereco());
    }

    @Test
    void deveRejeitarEdicaoClienteInexistente(){

        Cliente dadosAtualizado = new Cliente();

        dadosAtualizado.setNome("João Ramos");
        dadosAtualizado.setTelefone("-(94)-9999+++-99991");
        dadosAtualizado.setEndereco("Centro");


        when(repositoryPortOut.buscarPorId(1L))
                .thenReturn(Optional.empty());
        assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.editar(1L, dadosAtualizado)
        );

       verify(repositoryPortOut, never()).salvar(any(Cliente.class));
    }

    @Test
    void deveAceitarEditarClienteExistente() {
        Cliente clienteExistente = new Cliente();

        clienteExistente.setNome("João");
        clienteExistente.setTelefone("(91){}9-999+99999");
        clienteExistente.setEndereco("Centro");


        Cliente dadosAtualizado = new Cliente();

        dadosAtualizado.setNome("João Ramos");
        dadosAtualizado.setTelefone("-(94)-9999+++-99991");
        dadosAtualizado.setEndereco("Centro");

        when(repositoryPortOut.buscarPorId(1L))
                .thenReturn(Optional.of(clienteExistente));

        clienteService.editar(1L, dadosAtualizado);

        assertEquals("João Ramos", clienteExistente.getNome());
        assertEquals("94999999991", clienteExistente.getTelefone());
        assertEquals("Centro", clienteExistente.getEndereco());

        verify(repositoryPortOut, times(1)).buscarPorId(1L);
        verify(repositoryPortOut, times(1)).salvar(clienteExistente);
    }

    @Test
    void deveExcluirClienteExistente() {
        Cliente clienteExistente = new Cliente();

        clienteExistente.setNome("João");
        clienteExistente.setTelefone("(91){}9-999+99999");
        clienteExistente.setEndereco("Centro");

        when(repositoryPortOut.buscarPorId(1L))
                .thenReturn(Optional.of(clienteExistente));

        clienteService.excluir(1L);

        verify(repositoryPortOut, times(1)).excluir(1L);
    }

    @Test
    void deveRejeitarExclusaoClienteInexistente(){
        when(repositoryPortOut.buscarPorId(1L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.excluir(1L)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(repositoryPortOut, never()).excluir(1L);
    }

    @Test
    void deveListarClientes() {
        Cliente clienteExistente = new Cliente();

        clienteExistente.setNome("João");
        clienteExistente.setTelefone("(91){}9-999+99999");
        clienteExistente.setEndereco("Centro");

        Cliente cliente2 = new Cliente();

        cliente2.setNome("João Ramos");
        cliente2.setTelefone("-(94)-9999+++-99991");
        cliente2.setEndereco("Centro");

        List<Cliente> clientes = List.of(clienteExistente, cliente2);
        when(repositoryPortOut.listar())
                .thenReturn(clientes);

        List<Cliente> clientesExistentes = clienteService.listar();
        assertEquals(2, clientesExistentes.size());

        verify(repositoryPortOut, times(1)).listar();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemClientes() {

        List<Cliente> clientes = List.of();

        when(repositoryPortOut.listar())
                .thenReturn(clientes);

        List<Cliente> clientesRetornados = clienteService.listar();

        assertTrue(clientesRetornados.isEmpty());
        verify(repositoryPortOut, times(1)).listar();
    }
}
