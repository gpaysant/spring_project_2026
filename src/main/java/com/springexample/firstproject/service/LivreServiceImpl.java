package com.springexample.firstproject.service;

import com.springexample.firstproject.entity.Livre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreServiceImpl implements LivreService {

    private final List<Livre> livres = List.of(
            new Livre("1984", "Orwell", 1949),
            new Livre("Le Petit Prince", "Saint-Exupéry", 1943)
    );

    @Override
    public List<Livre> getAllLivres() {
        return livres;
    }

    @Override
    public Livre getLivreByTitre(String titre) {
        return livres.stream()
                .filter(livre -> livre.title().equalsIgnoreCase(titre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
    }
}