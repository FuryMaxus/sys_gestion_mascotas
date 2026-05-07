package com.sanosysalvos.servicio_gestion.service;

import com.sanosysalvos.servicio_gestion.exception.ResourceNotFoundException;
import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.repository.PetRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackFindAll")
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackFindById")
    public Pet findById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Pet not found with ID: " + id));
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackEmptyList")
    public List<Pet> findByOwnerId(UUID ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackEmptyList")
    public List<Pet> findByRun(String run) {
        return petRepository.findByRun(run);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackEmptyList")
    public List<Pet> findbyStatus(Status status) {
        return petRepository.findByStatus(status);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackGenericError")
    public Pet save(Pet pet){
        return petRepository.save(pet);
    }

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackGenericError")
    public Pet update(UUID id, Pet pet){
        Pet existingPet = findById(id);
        existingPet.setRun(pet.getRun());
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

    @CircuitBreaker(name = "dbPetCircuitBreaker", fallbackMethod = "fallbackDelete")
    public void delete(UUID id) {
        petRepository.deleteById(id);
    }

    private List<Pet> fallbackEmptyList(Throwable e) {
        return Collections.emptyList();
    }

    private Pet fallbackFindById(UUID id, Throwable e) {
        throw new RuntimeException("La base de datos de mascotas no está disponible.");
    }

    private Pet fallbackGenericError(Pet pet, Throwable e) {
        throw new RuntimeException("Error crítico: No se pudo persistir la información.");
    }

    private void fallbackDelete(UUID id, Throwable e) {
        throw new RuntimeException("No se pudo eliminar la mascota. Intente más tarde.");
    }


}
