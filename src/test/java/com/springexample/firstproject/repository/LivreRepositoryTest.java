package com.springexample.firstproject.repository;

import com.springexample.firstproject.entity.Livre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LivreRepositoryTest {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindLivreByTitre() {
        livreRepository.save(Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build());

        Optional<Livre> result = livreRepository.findByTitre("Dune");

        assertTrue(result.isPresent());
        assertEquals("Herbert", result.get().getAuteur());
    }

    @Test
    void shouldReturnEmptyWhenTitreUnknown() {
        assertTrue(livreRepository.findByTitre("Inconnu").isEmpty());
    }

    @Test
    void shouldCreateAndCheckLivre() {
        livreRepository.save(Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build());

        Optional<Livre> result = livreRepository.findByTitre("Dune");

        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
    }

    @Test
    void shouldGenerateIdWhenSavingLivre() {
        livreRepository.save(Livre.builder().titre("Dune").auteur("Herbert").annee(1965).build());
        entityManager.flush();
        entityManager.clear();

        Optional<Livre> result = livreRepository.findByTitre("Dune");

        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals("Herbert", result.get().getAuteur());
    }
}
