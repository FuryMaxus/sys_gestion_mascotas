package com.sanosysalvos.servicio_gestion.service;

import com.sanosysalvos.servicio_gestion.exception.ResourceNotFoundException;
import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.repository.PetRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackFindById")
    public Pet findById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("No se encontró la mascota con el ID: " + id));
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackFindPets")
    public List<Pet> findPets(UUID ownerId, Status status) {
        return petRepository.findWithFilters(ownerId, status);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackSave")    public Pet save(Pet pet){
        return petRepository.save(pet);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackUpdate")
    public Pet update(UUID id, @NonNull Pet pet){
        Pet existingPet = this.findById(id);
        existingPet.setName(pet.getName());
        existingPet.setStatus(pet.getStatus());
        existingPet.setSpecies(pet.getSpecies());
        existingPet.setColor(pet.getColor());
        existingPet.setSize(pet.getSize());
        existingPet.setFoundLocation(pet.getFoundLocation());
        existingPet.setLostLocation(pet.getLostLocation());
        existingPet.setDescription(pet.getDescription());
        existingPet.setOwnerId(pet.getOwnerId());
        return save(existingPet);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackPatch")
    public Pet patch(UUID id, @NonNull Pet partialPet){
        Pet existingPet = this.findById(id);

        if (partialPet.getName() != null) existingPet.setName(partialPet.getName());
        if (partialPet.getStatus() != null) existingPet.setStatus(partialPet.getStatus());
        if (partialPet.getSpecies() != null) existingPet.setSpecies(partialPet.getSpecies());
        if (partialPet.getColor() != null) existingPet.setColor(partialPet.getColor());
        if (partialPet.getSize() != null) existingPet.setSize(partialPet.getSize());
        if (partialPet.getFoundLocation() != null) existingPet.setFoundLocation(partialPet.getFoundLocation());
        if (partialPet.getLostLocation() != null) existingPet.setLostLocation(partialPet.getLostLocation());
        if (partialPet.getDescription() != null) existingPet.setDescription(partialPet.getDescription());
        if (partialPet.getOwnerId() != null) existingPet.setOwnerId(partialPet.getOwnerId());

        return petRepository.save(existingPet);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackDelete")
    public void delete(UUID id) {
        Pet existingPet = this.findById(id);
        petRepository.delete(existingPet);
    }

    private Pet fallbackFindById(UUID id, Throwable e) {
        if (e instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) e;
        }
        throw new RuntimeException("La base de datos de mascotas no está disponible.");
    }

    private List<Pet> fallbackFindPets(UUID ownerId, Status status, Throwable e) {
        return Collections.emptyList();
    }

    private Pet fallbackSave(Pet pet, Throwable e) {
        throw new RuntimeException("Error crítico: No se pudo guardar la mascota.");
    }

    private Pet fallbackUpdate(UUID id, Pet pet, Throwable e) {
        if (e instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) e;
        }
        throw new RuntimeException("Error crítico: No se pudo actualizar la mascota.");
    }

    private Pet fallbackPatch(UUID id, Pet partialPet, Throwable e) {
        if (e instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) e;
        }
        throw new RuntimeException("Error crítico: No se pudo modificar la mascota parcialmente.");
    }

    private void fallbackDelete(UUID id, Throwable e) {
        if (e instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) e;
        }
        throw new RuntimeException("No se pudo eliminar la mascota. Intente más tarde.");
    }

}
