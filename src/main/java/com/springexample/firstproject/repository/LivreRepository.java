package com.springexample.firstproject.repository;

import com.springexample.firstproject.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivreRepository extends JpaRepository<Livre, Long> {

    Optional<Livre> findByTitre(String titre);
}
