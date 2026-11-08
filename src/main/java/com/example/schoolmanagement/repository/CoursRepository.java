package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, Long> {
    // Requête personnalisée 2 : cours assurés par un enseignant donné.
    List<Cours> findByEnseignantId(Long enseignantId);
    List<Cours> findByClasseId(Long classeId);
}
