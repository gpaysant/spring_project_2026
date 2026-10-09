package com.springexample.firstproject.service;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.exception.LivreNotFoundException;
import com.springexample.firstproject.repository.LivreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivreServiceImplTest {

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private LivreServiceImpl livreService;

    @Test
    void shouldReturnAllLivres() {
        List<Livre> livres = List.of(
                Livre.builder().titre("1984").auteur("Orwell").annee(1949).build(),
                Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build()
        );
        when(livreRepository.findAll()).thenReturn(livres);

        List<Livre> result = livreService.getAllLivres();

        assertEquals(2, result.size());
        verify(livreRepository).findAll();
    }

    @Test
    void shouldThrowWhenLivreNotFound() {
        when(livreRepository.findByTitre("Inconnu")).thenReturn(Optional.empty());

        assertThrows(LivreNotFoundException.class,
                () -> livreService.getLivreByTitre("Inconnu"));
    }

    @Test
    void shouldReturnLivre() {
        Livre livre = Livre.builder().titre("1984").auteur("Orwell").annee(1949).build();
        when(livreRepository.findByTitre("1984")).thenReturn(Optional.of(livre));
        Livre result = livreService.getLivreByTitre("1984");

        assertEquals("Orwell", result.getAuteur());
        assertEquals("1984", result.getTitre());
        assertEquals(1949, result.getAnnee());
        verify(livreRepository).findByTitre("1984");
    }

    @Test
    void shouldCreateLivre() {
        Livre livre = Livre.builder().titre("1984").auteur("Orwell").annee(1949).build();
        when(livreRepository.save(livre)).thenReturn(livre);
        Livre result = livreService.createLivre(livre);

        assertSame(livre, result);
        verify(livreRepository).save(livre);
    }

    @Test
    void shouldUpdateLivre() {
        Livre existing = Livre.builder().titre("1984").auteur("Orwell").annee(1949).build();
        existing.setId(1L);
        when(livreRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(livreRepository.save(any(Livre.class))).thenAnswer(inv -> inv.getArgument(0));

        Livre modifications = Livre.builder().titre("Nineteen Eighty-Four").auteur("George Orwell").annee(1960).build();
        Livre result = livreService.updateLivre(1L, modifications);

        assertEquals("Nineteen Eighty-Four", result.getTitre());
        assertEquals("George Orwell", result.getAuteur());
        assertEquals(1960, result.getAnnee());
        assertEquals(1L, result.getId());
        verify(livreRepository).save(existing);
    }

    @Test
    void shouldThrowWhenLivreToUpdateNotFound() {
        when(livreRepository.findById(2L)).thenReturn(Optional.empty());
        Livre modifications = Livre.builder().titre("Nineteen Eighty-Four").auteur("George Orwell").annee(1960).build();

        assertThrows(LivreNotFoundException.class, () -> livreService.updateLivre(2L, modifications));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenLivreToDeleteNotFound() {
        when(livreRepository.existsById(1L)).thenReturn(false);

        assertThrows(LivreNotFoundException.class, () -> livreService.deleteLivre(1L));
        verify(livreRepository, never()).deleteById(any());
    }

    @Test
    void shouldDeleteLivre() {
        when(livreRepository.existsById(1L)).thenReturn(true);

        livreService.deleteLivre(1L);

        verify(livreRepository).deleteById(1L);
    }

}
