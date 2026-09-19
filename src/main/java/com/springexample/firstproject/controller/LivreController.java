package com.springexample.firstproject.controller;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.service.LivreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LivreController {

    private final LivreService livreService;

    public LivreController (LivreService livreService) {
        this.livreService = livreService;
    }

    @GetMapping ("/livres")
    public List<Livre> getLivres() {
        return livreService.getAllLivres();
    }

    @GetMapping ("/livres/{title}")
    public Livre getLivre(@PathVariable String title) {
        return livreService.getLivreByTitre(title);
    }

}