package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.Inscription;
import com.example.schoolmanagement.repository.InscriptionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;

    public InscriptionService(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    public List<Inscription> findAll() {
        return inscriptionRepository.findAll();
    }

    public Inscription findById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription introuvable avec l'id : " + id));
    }

    public Inscription save(Inscription objet) {
        return inscriptionRepository.save(objet);
    }

    public Inscription update(Long id, Inscription objet) {
        if (!inscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscription introuvable avec l'id : " + id);
        }
        objet.setId(id);
        return inscriptionRepository.save(objet);
    }

    public void delete(Long id) {
        if (!inscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscription introuvable avec l'id : " + id);
        }
        inscriptionRepository.deleteById(id);
    }
}
