package com.springexample.firstproject.service;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.repository.LivreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;

    public LivreServiceImpl(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @Override
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    @Override
    public Livre getLivreByTitre(String titre) {
        return livreRepository.findAll().stream()
                .filter(l -> l.getTitre().equalsIgnoreCase(titre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
    }
}