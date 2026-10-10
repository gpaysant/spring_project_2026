package com.springexample.firstproject.controller;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.exception.LivreNotFoundException;
import com.springexample.firstproject.service.LivreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivreController.class)
class LivreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivreService livreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllLivres() throws Exception {
        when(livreService.getAllLivres()).thenReturn(List.of(
                Livre.builder().titre("1984").auteur("Orwell").annee(1949).build(),
                Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build()
        ));

        mockMvc.perform(get("/livres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titre").value("1984"));
    }

    @Test
    void shouldReturn404WhenLivreNotFound() throws Exception {
        when(livreService.getLivreByTitre("Inconnu"))
                .thenThrow(new LivreNotFoundException("Livre non trouvé : Inconnu"));

        mockMvc.perform(get("/livres/Inconnu"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Livre non trouvé : Inconnu"));
    }

    @Test
    void shouldCreateLivre() throws Exception {
        Livre livre = Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build();
        when(livreService.createLivre(any(Livre.class))).thenReturn(livre);

        mockMvc.perform(post("/livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Dune"));

        verify(livreService).createLivre(any(Livre.class));
    }

   @Test
   void shouldUpdateLivre() throws Exception {
       Long livreId = 1L;
       Livre input = Livre.builder().id(livreId).titre("1984").auteur("Orwell").annee(1949).build();

       Livre updated = Livre.builder().id(livreId).titre("1984").auteur("Bernard").annee(1985).build();
       when(livreService.updateLivre(eq(livreId), any(Livre.class))).thenReturn(updated);

       mockMvc.perform(put("/livres/{id}", livreId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(input)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(livreId))
               .andExpect(jsonPath("$.auteur").value("Bernard"))
               .andExpect(jsonPath("$.annee").value(1985));
   }

    @Test
    void shouldReturn404WhenLivreToUpdateNotFound() throws Exception {
        Long livreId = 99L;
        Livre input = Livre.builder().id(livreId).titre("1984").auteur("Orwell").annee(1949).build();
        when(livreService.updateLivre(eq(livreId), any(Livre.class))).thenThrow(new LivreNotFoundException("Livre non trouvé : " + livreId));

        mockMvc.perform(put("/livres/{id}", livreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Livre non trouvé : " + livreId));
    }

    @Test
    void shouldDeleteLivre() throws Exception {
        Long livreId = 1L;

        mockMvc.perform(delete("/livres/{id}", livreId))
                .andExpect(status().isOk());

        verify(livreService).deleteLivre(livreId);
    }

    @Test
    void shouldReturn404WhenLivreNotFoundForDeleteLivre() throws Exception {
        Long livreId = 99L;
        doThrow(new LivreNotFoundException("Livre non trouvé, id : " + livreId))
                .when(livreService).deleteLivre(livreId);

        mockMvc.perform(delete("/livres/{id}", livreId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Livre non trouvé, id : " + livreId));

        verify(livreService).deleteLivre(livreId);
    }

    @Test
    void shouldReturn500WithGenericMessageOnUnexpectedError() throws Exception {
        when(livreService.getAllLivres()).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/livres"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Une erreur est survenue"))
                .andExpect(content().string(not(containsString("boom"))));
    }

    @Test
    void shouldReturn400WithGenericMessageOnUnexpectedError() throws Exception {
        mockMvc.perform(get("/liv"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ressource introuvable"));
    }
}
