package com.consultapet.petcare.pet.infrastructure.persistence;

import com.consultapet.petcare.pet.domain.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
