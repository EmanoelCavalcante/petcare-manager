package com.consultapet.petcare.pet.application.port.out;

import com.consultapet.petcare.pet.domain.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepositoryPortOut {

    Pet salvar(Pet pet);

    List<Pet> listar();


    Optional<Pet> buscarPorId(Long id);

    void deletar(Long id);
}
