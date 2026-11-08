package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    // Requête personnalisée 1 : rechercher les étudiants par nom (recherche partielle).
    List<Etudiant> findByNomContainingIgnoreCase(String nom);
    boolean existsByEmailIgnoreCase(String email);
}
