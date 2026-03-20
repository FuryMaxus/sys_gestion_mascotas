package com.sanosysalvos.servicio_gestion.repository;

import com.sanosysalvos.servicio_gestion.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {

}
