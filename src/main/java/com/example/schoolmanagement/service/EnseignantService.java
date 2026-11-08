package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.Enseignant;
import com.example.schoolmanagement.repository.EnseignantRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;

    public EnseignantService(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
    }

    public List<Enseignant> findAll() {
        return enseignantRepository.findAll();
    }

    public Enseignant findById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant introuvable avec l'id : " + id));
    }

    public Enseignant save(Enseignant objet) {
        return enseignantRepository.save(objet);
    }

    public Enseignant update(Long id, Enseignant objet) {
        if (!enseignantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enseignant introuvable avec l'id : " + id);
        }
        objet.setId(id);
        return enseignantRepository.save(objet);
    }

    public void delete(Long id) {
        if (!enseignantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enseignant introuvable avec l'id : " + id);
        }
        enseignantRepository.deleteById(id);
    }
}
