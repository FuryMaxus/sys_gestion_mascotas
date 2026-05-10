package com.sanosysalvos.servicio_gestion.service;

import com.sanosysalvos.servicio_gestion.exception.ResourceNotFoundException;
import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    private Pet samplePet;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        samplePet = new Pet(sampleId, "Firulais", Status.LOST, "Perro", "Café", 15.0,
                 "Collar azul", UUID.randomUUID());
    }

    @Test
    void findById_WhenPetExists_ShouldReturnPet() {
        when(petRepository.findById(sampleId)).thenReturn(Optional.of(samplePet));

        Pet result = petService.findById(sampleId);

        assertNotNull(result);
        assertEquals("Firulais", result.getName());
        verify(petRepository, times(1)).findById(sampleId);
    }

    @Test
    void findById_WhenPetDoesNotExist_ShouldThrowException() {
        when(petRepository.findById(sampleId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.findById(sampleId);
        });

        assertTrue(exception.getMessage().contains("No se encontró la mascota"));
        verify(petRepository, times(1)).findById(sampleId);
    }

    @Test
    void findPets_ShouldReturnList() {
        when(petRepository.findWithFilters(any(), any())).thenReturn(List.of(samplePet));

        List<Pet> result = petService.findPets(null, null);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void update_WhenPetExists_ShouldReturnUpdatedPet() {
        when(petRepository.findById(sampleId)).thenReturn(Optional.of(samplePet));
        when(petRepository.save(any(Pet.class))).thenReturn(samplePet);

        Pet result = petService.update(sampleId, samplePet);

        assertNotNull(result);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void patch_WhenPetExists_ShouldUpdateOnlyNonNullFields() {
        Pet existingPet = new Pet();
        existingPet.setId(sampleId);
        existingPet.setName("Nombre Antiguo");
        existingPet.setStatus(Status.LOST);

        Pet partialUpdate = new Pet();
        partialUpdate.setName("Nombre Nuevo");

        when(petRepository.findById(sampleId)).thenReturn(Optional.of(existingPet));
        when(petRepository.save(any(Pet.class))).thenAnswer(i -> i.getArguments()[0]);

        Pet result = petService.patch(sampleId, partialUpdate);

        assertEquals("Nombre Nuevo", result.getName());
        assertEquals(Status.LOST, result.getStatus());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void delete_WhenPetExists_ShouldInvokeDelete() {
        when(petRepository.findById(sampleId)).thenReturn(Optional.of(samplePet));
        doNothing().when(petRepository).delete(samplePet);

        assertDoesNotThrow(() -> petService.delete(sampleId));
        verify(petRepository, times(1)).delete(samplePet);
    }

    @Test
    void fallbackFindById_ShouldThrowRuntimeException() {
        Exception exception = new RuntimeException("DB Down");

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            petService.fallbackFindById(sampleId, exception);
        });

        assertEquals("La base de datos de mascotas no está disponible.", thrown.getMessage());
    }

    @Test
    void fallbackFindPets_ShouldReturnEmptyList() {
        Exception exception = new RuntimeException("Error de conexión");
        UUID dummyOwnerId = UUID.randomUUID();
        Status dummyStatus = Status.LOST;

        List<Pet> result = petService.fallbackFindPets(dummyOwnerId, dummyStatus, exception);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fallbackSave_ShouldThrowRuntimeException() {
        Exception exception = new RuntimeException("DB Connection Reset");
        Pet pet = new Pet();

        assertThrows(RuntimeException.class, () -> {
            petService.fallbackSave(pet, exception);
        });
    }

    @Test
    void fallbackUpdate_ShouldThrowRuntimeException() {
        Exception exception = new RuntimeException("Neon DB Timeout during update");
        Pet petToUpdate = new Pet();
        petToUpdate.setName("Firulais Actualizado");

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            petService.fallbackUpdate(sampleId, petToUpdate, exception);
        });

        assertTrue(thrown.getMessage().contains("Error crítico: No se pudo actualizar la mascota."));
    }

    @Test
    void fallbackPatch_ShouldThrowRuntimeException() {
        Exception exception = new RuntimeException("DB Timeout");
        Pet pet = new Pet();

        assertThrows(RuntimeException.class, () -> {
            petService.fallbackPatch(sampleId, pet, exception);
        });
    }

    @Test
    void fallbackDelete_ShouldThrowRuntimeException() {
        Exception exception = new RuntimeException("DB Error");

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            petService.fallbackDelete(sampleId, exception);
        });

        assertEquals("No se pudo eliminar la mascota. Intente más tarde.", thrown.getMessage());
    }
}
