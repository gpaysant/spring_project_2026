package com.springexample.firstproject.service;

import com.springexample.firstproject.entity.Livre;

import java.util.List;

public interface LivreService {
    List<Livre> getAllLivres();
    Livre getLivreByTitre(String titre);
}
