package com.springexample.firstproject.controller;

import com.springexample.firstproject.entity.Livre;
import com.springexample.firstproject.service.LivreService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/livres")
    public Livre addLivre(@RequestBody Livre livre) {
        return livreService.createLivre(livre);
    }

    @PutMapping("/livres/{id}")
    public Livre updateLivre(@PathVariable Long id, @RequestBody Livre livre) {
        return livreService.updateLivre(id, livre);
    }

    @DeleteMapping("/livres/{id}")
    public void deleteLivre(@PathVariable Long id) {
        livreService.deleteLivre(id);
    }

}