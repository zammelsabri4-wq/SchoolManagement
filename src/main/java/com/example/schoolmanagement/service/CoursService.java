package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.Cours;
import com.example.schoolmanagement.repository.CoursRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoursService {

    private final CoursRepository coursRepository;

    public CoursService(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    public Cours findById(Long id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable avec l'id : " + id));
    }

    public Cours save(Cours objet) {
        return coursRepository.save(objet);
    }

    public Cours update(Long id, Cours objet) {
        if (!coursRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cours introuvable avec l'id : " + id);
        }
        objet.setId(id);
        return coursRepository.save(objet);
    }

    public void delete(Long id) {
        if (!coursRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cours introuvable avec l'id : " + id);
        }
        coursRepository.deleteById(id);
    }
}
