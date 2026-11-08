package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    // Requête personnalisée 3 : inscriptions d'un étudiant.
    List<Inscription> findByEtudiantId(Long etudiantId);
    List<Inscription> findByCoursId(Long coursId);
}
