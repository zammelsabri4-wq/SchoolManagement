package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
