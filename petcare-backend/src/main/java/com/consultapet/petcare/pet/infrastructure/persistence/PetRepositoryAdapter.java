package com.consultapet.petcare.pet.infrastructure.persistence;

import com.consultapet.petcare.pet.application.port.out.PetRepositoryPortOut;
import com.consultapet.petcare.pet.domain.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PetRepositoryAdapter implements PetRepositoryPortOut {
    private final PetRepository repository;

    public PetRepositoryAdapter(PetRepository repository) {
        this.repository = repository;
    }
    @Override
    public Pet salvar(Pet pet){
        return repository.save(pet);
    }

    @Override
    public List<Pet> listar(){
        return repository.findAll();
    }

    @Override
    public Optional<Pet> buscarPorId(Long id){
        return repository.findById(id);
    }

    @Override
    public void deletar(Long id){
        repository.deleteById(id);
    }
}
