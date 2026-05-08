package com.sanosysalvos.servicio_gestion.controller;

import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pets")
@Tag(name = "Mascotas", description = "Operaciones para la gestión y búsqueda de mascotas rescatadas")
public class PetController {

    private final PetService petService;

    @Operation(
            summary = "Obtener lista de mascotas",
            description = "Devuelve todas las mascotas. Permite filtrar opcionalmente por ID del dueño o Estado."
    )
    @GetMapping()
    public ResponseEntity<List<Pet>> findPets(
            @RequestParam(required = false) UUID ownerId,
            @RequestParam(required = false) Status status
    ) {
        List<Pet> pets = petService.findPets(ownerId, status);
        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(
            summary = "Obtener una mascota",
            description = "Devuelve todos los datos de una mascota especifica segun la id entregada"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Pet> findPetById(
            @PathVariable UUID id
    ) {
        Pet pet = petService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @Operation(
            summary = "Actualizar una mascota",
            description = "Actualiza todos los datos de una mascota especifica"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(
            @PathVariable UUID id,
            @RequestBody Pet pet
    ) {
        Pet updatedPet = petService.update(id, pet);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(
            summary = "Modificar una mascota parcialmente",
            description = "Modifica los datos de una mascota, solo modifica los parametros que se envien."
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Pet> patchPet(
            @PathVariable UUID id,
            @RequestBody Pet partialPet
    ) {
        Pet updatedPet = petService.patch(id, partialPet);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(
            summary = "Registrar nueva mascota",
            description = "Crea un nuevo registro de mascota en el sistema."
    )
    @PostMapping()
    public ResponseEntity<Pet> addPet(
            @RequestBody Pet pet
    ) {
        Pet savedPet = petService.save(pet);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(savedPet);
    }

    @Operation(
            summary = "Eliminar una mascota",
            description = "Elimina los datos de una mascota en el sistema."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(
            @PathVariable UUID id
    ){
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
