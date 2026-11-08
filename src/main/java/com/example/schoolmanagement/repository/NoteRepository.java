package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByInscriptionEtudiantId(Long etudiantId);
}
