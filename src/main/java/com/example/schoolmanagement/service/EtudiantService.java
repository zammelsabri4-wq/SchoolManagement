package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.Etudiant;
import com.example.schoolmanagement.repository.EtudiantRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant introuvable avec l'id : " + id));
    }

    public Etudiant save(Etudiant objet) {
        return etudiantRepository.save(objet);
    }

    public Etudiant update(Long id, Etudiant objet) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Etudiant introuvable avec l'id : " + id);
        }
        objet.setId(id);
        return etudiantRepository.save(objet);
    }

    public void delete(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Etudiant introuvable avec l'id : " + id);
        }
        etudiantRepository.deleteById(id);
    }
}
