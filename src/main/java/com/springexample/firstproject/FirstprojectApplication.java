package com.springexample.firstproject;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.repository.LivreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstprojectApplication implements CommandLineRunner {

	private final LivreRepository livreRepository;

    public FirstprojectApplication(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    public static void main(String[] args) {

		SpringApplication.run(FirstprojectApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		livreRepository.save(Livre.builder().titre("1984").auteur("Georges Orwell").annee(1949).build());
		livreRepository.save(Livre.builder().titre("Le Seigneur des Anneaux").auteur("J.R.R. Tolkien").annee(1954).build());
	}
}
