package com.consultapet.petcare.pet.domain.model;

import com.consultapet.petcare.client.domain.model.Cliente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pets")
@Getter
@Setter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomePet;

    private BigDecimal peso;

    private String raca;

    @Column(nullable = false)
    private String especie;

    private Integer idadeEstimada;

    private LocalDate dataEstimativa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
