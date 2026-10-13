package com.consultapet.petcare.client.infrastructure.persistence;

import com.consultapet.petcare.client.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Cliente, Long> {

}
