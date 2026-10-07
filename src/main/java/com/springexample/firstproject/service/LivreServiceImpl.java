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
        return livreRepository.findByTitre(titre)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        /*return livreRepository.findAll().stream()
                .filter(l -> l.getTitre().equalsIgnoreCase(titre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));*/
    }

    @Override
    public Livre createLivre(Livre livre) {
        return livreRepository.save(livre);
    }

    @Override
    public Livre updateLivre(Long id, Livre livre) {
        Livre livreExistant = livreRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Livre non trouvé"));
        livreExistant.setAnnee(livre.getAnnee());
        livreExistant.setAuteur(livre.getAuteur());
        livreExistant.setTitre(livre.getTitre());
        return livreRepository.save(livreExistant);
    }

    @Override
    public void deleteLivre(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new RuntimeException("Livre non trouvé");
        }
        livreRepository.deleteById(id);
    }
}