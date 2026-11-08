package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.Note;
import com.example.schoolmanagement.repository.NoteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note introuvable avec l'id : " + id));
    }

    public Note save(Note objet) {
        return noteRepository.save(objet);
    }

    public Note update(Long id, Note objet) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note introuvable avec l'id : " + id);
        }
        objet.setId(id);
        return noteRepository.save(objet);
    }

    public void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note introuvable avec l'id : " + id);
        }
        noteRepository.deleteById(id);
    }
}
