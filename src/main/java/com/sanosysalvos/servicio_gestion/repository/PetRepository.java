package com.sanosysalvos.servicio_gestion.repository;

import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    @Query("SELECT p FROM Pet p WHERE " +
            "(:ownerId IS NULL OR p.ownerId = :ownerId) AND " +
            "(:status IS NULL OR p.status = :status)")
    List<Pet> findWithFilters(
            @Param("ownerId") UUID ownerId,
            @Param("status") Status status
    );
}
