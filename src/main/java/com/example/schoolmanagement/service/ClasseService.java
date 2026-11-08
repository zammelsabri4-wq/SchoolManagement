package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.Classe;
import com.example.schoolmanagement.repository.ClasseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClasseService {

    private final ClasseRepository classeRepository;

    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    public List<Classe> findAll() {
        return classeRepository.findAll();
    }

    public Classe findById(Long id) {
        return classeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe introuvable avec l'id : " + id));
    }

    public Classe save(Classe objet) {
        return classeRepository.save(objet);
    }

    public Classe update(Long id, Classe objet) {
        if (!classeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Classe introuvable avec l'id : " + id);
        }
        objet.setId(id);
        return classeRepository.save(objet);
    }

    public void delete(Long id) {
        if (!classeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Classe introuvable avec l'id : " + id);
        }
        classeRepository.deleteById(id);
    }
}
