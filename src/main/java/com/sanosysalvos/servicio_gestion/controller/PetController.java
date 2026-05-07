package com.sanosysalvos.servicio_gestion.controller;

import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pets")
public class PetController {

    private final PetService petService;

    @GetMapping()
    public ResponseEntity<List<Pet>> findPets(
            @RequestParam(required = false) UUID ownerId,
            @RequestParam(required = false) String run,
            @RequestParam(required = false) Status status
    ) {
        List<Pet> pets;
        if (ownerId != null) {
            pets = petService.findByOwnerId(ownerId);
        } else if (run != null) {
            pets = petService.findByRun(run);
        } else if (status != null) {
            pets = petService.findbyStatus(status);
        } else {
            pets = petService.findAll();
        }
        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> findPetById(
            @PathVariable UUID id
    ) {
        Pet pet = petService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(
            @PathVariable UUID id,
            @RequestBody Pet pet
    ) {
        Pet updatedPet = petService.update(id, pet);
        return ResponseEntity.ok(updatedPet);
    }

    @PostMapping()
    public ResponseEntity<Pet> addPet(
            @RequestBody Pet pet
    ) {
        Pet savedPet = petService.save(pet);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(savedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(
            @PathVariable UUID id
    ){
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
