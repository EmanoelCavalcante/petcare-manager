package com.consultapet.petcare.client.infrastructure.persistence;

import com.consultapet.petcare.client.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Cliente, Long> {

}
