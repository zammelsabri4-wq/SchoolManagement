package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Classe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasseRepository extends JpaRepository<Classe, Long> {
    boolean existsByNomIgnoreCase(String nom);
}
