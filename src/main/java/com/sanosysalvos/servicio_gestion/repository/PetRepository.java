package com.sanosysalvos.servicio_gestion.repository;

import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    List<Pet> findByRun(String run);
    List<Pet> findByStatus(Status status);
    List<Pet> findByOwnerId(UUID ownerId);
}
