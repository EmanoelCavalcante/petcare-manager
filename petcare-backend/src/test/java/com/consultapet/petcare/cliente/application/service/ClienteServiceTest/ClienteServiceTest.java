package com.consultapet.petcare.cliente.application.service.ClienteServiceTest;

import com.consultapet.petcare.client.application.port.out.ClienteRepositoryPortOut;
import com.consultapet.petcare.client.application.service.ClientService;
import com.consultapet.petcare.client.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteServiceTest {

    private final ClienteRepositoryPortOut repositoryPortOut = Mockito.mock(ClienteRepositoryPortOut.class);
    private final ClientService clienteService = new ClientService(repositoryPortOut);


    @Test
    void deveRejeitarClienteSemNome() {

        Cliente cliente = new Cliente();

        cliente.setNome("   ");
        cliente.setTelefone("91999999999");
        cliente.setEndereco("Centro");

        assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.salvar(cliente)

        );
    }
}
