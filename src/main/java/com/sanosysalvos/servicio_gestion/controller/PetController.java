package com.sanosysalvos.servicio_gestion.controller;

import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping()
    public ResponseEntity<List<Pet>> findAll() {
        List<Pet> pets = petService.findAll();
        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets);
    }

    @PostMapping()
    public ResponseEntity<Pet> addPet(
            @RequestBody Pet pet
    ) {
        Pet savedPet = petService.save(pet);
        return ResponseEntity.ok(savedPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(
            @PathVariable Long id
    ){
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
