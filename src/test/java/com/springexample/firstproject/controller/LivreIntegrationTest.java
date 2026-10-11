package com.springexample.firstproject.controller;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.repository.LivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LivreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        livreRepository.deleteAll();
    }

    @Test
    void shouldCreateThenRetrieveLivre() throws Exception {
        Livre livre = Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build();

        mockMvc.perform(post("/livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());

        mockMvc.perform(get("/livres/Dune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auteur").value("Herbert"));

        assertEquals(1, livreRepository.count());
    }

    @Test
    void shouldReturn404WhenLivreDoesNotExist() throws Exception {
        mockMvc.perform(get("/livres/Inconnu"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateThenModifyThenRetrieveLivre() throws Exception {
        Livre livre = Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build();

        String json = mockMvc.perform(post("/livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livre)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long livreId = objectMapper.readValue(json, Livre.class).getId();

        Livre updatedLivre = Livre.builder().titre("Lune").auteur("Bebert").annee(1970).build();

        mockMvc.perform(put("/livres/{id}", livreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLivre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livreId))
                .andExpect(jsonPath("$.titre").value(updatedLivre.getTitre()))
                .andExpect(jsonPath("$.auteur").value(updatedLivre.getAuteur()))
                .andExpect(jsonPath("$.annee").value(updatedLivre.getAnnee()));

        mockMvc.perform(get("/livres/Dune"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/livres/Lune"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value(updatedLivre.getTitre()))
                .andExpect(jsonPath("$.auteur").value(updatedLivre.getAuteur()))
                .andExpect(jsonPath("$.annee").value(updatedLivre.getAnnee()));

        assertEquals(1, livreRepository.count());
    }

    @Test
    void shouldCreateThenDeleteLivre() throws Exception {
        Livre livre = Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build();

        ResultActions result = mockMvc.perform(post("/livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());

        String json = mockMvc.perform(post("/livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livre)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long livreId = objectMapper.readValue(json, Livre.class).getId();

        mockMvc.perform(delete("/livres/{id}", livreId))
                .andExpect(status().isOk());

        assertEquals(0, livreRepository.count());
    }

    @Test
    void shouldReturn404WhenUpdatingUnknownLivre() throws Exception {
        Livre livre = Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build();
        mockMvc.perform(put("/livres/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livre)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404DeleteLivre() throws Exception {
        mockMvc.perform(delete("/livres/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
