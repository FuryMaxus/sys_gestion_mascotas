package com.sanosysalvos.servicio_gestion.service;

import com.sanosysalvos.servicio_gestion.exception.ResourceNotFoundException;
import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.repository.PetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet findById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Pet not found with ID: " + id));
    }

    public List<Pet> findByOwnerId(UUID ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    public List<Pet> findByRun(String run) {
        return petRepository.findByRun(run);
    }

    public List<Pet> findbyStatus(Status status) {
        return petRepository.findByStatus(status);
    }

    public Pet save(Pet pet){
        return petRepository.save(pet);
    }

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
    public void delete(UUID id) {
        petRepository.deleteById(id);
    }

}
