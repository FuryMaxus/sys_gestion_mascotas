package com.sanosysalvos.servicio_gestion.controller;

import com.sanosysalvos.servicio_gestion.model.Pet;
import com.sanosysalvos.servicio_gestion.model.Status;
import com.sanosysalvos.servicio_gestion.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    private Pet samplePet;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        samplePet = new Pet(sampleId,"Firulais", Status.LOST, "Perro", "Café", 15.0,
                 "Collar azul", UUID.randomUUID());
    }

    @Test
    @WithMockUser
    void getPets_ShouldReturn200AndList() throws Exception {
        Mockito.when(petService.findPets(any(), any()))
                .thenReturn(List.of(samplePet));

        mockMvc.perform(get("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Firulais"));
    }

    @Test
    @WithMockUser
    void getPetById_ShouldReturn200AndPet() throws Exception {
        Mockito.when(petService.findById(sampleId)).thenReturn(samplePet);

        mockMvc.perform(get("/api/v1/pets/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"));
    }

    @Test
    @WithMockUser
    void createPet_ShouldReturn201AndCreatedPet() throws Exception {
        Mockito.when(petService.save(any(Pet.class))).thenReturn(samplePet);

        mockMvc.perform(post("/api/v1/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(samplePet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Firulais"));
    }

    @Test
    @WithMockUser
    void updatePet_ShouldReturn200AndUpdatedPet() throws Exception {
        Mockito.when(petService.update(eq(sampleId), any(Pet.class))).thenReturn(samplePet);

        mockMvc.perform(put("/api/v1/pets/{id}", sampleId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(samplePet)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void patchPet_ShouldReturn200AndUpdatedPet() throws Exception {
        Mockito.when(petService.patch(eq(sampleId), any(Pet.class))).thenReturn(samplePet);

        mockMvc.perform(patch("/api/v1/pets/{id}", sampleId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nuevo Nombre\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deletePet_ShouldReturn204NoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/pets/{id}", sampleId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Mockito.verify(petService).delete(sampleId);
    }
}
