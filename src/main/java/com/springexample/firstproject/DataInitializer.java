package com.springexample.firstproject;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.repository.LivreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LivreRepository livreRepository;

    public DataInitializer(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @Override
    public void run(String... args) {
        livreRepository.save(Livre.builder().titre("1984").auteur("George Orwell").annee(1949).build());
        livreRepository.save(Livre.builder().titre("Le Seigneur des Anneaux").auteur("J.R.R. Tolkien").annee(1954).build());
    }
}
