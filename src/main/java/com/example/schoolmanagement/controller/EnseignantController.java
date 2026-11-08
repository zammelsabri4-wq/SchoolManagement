package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Enseignant;
import com.example.schoolmanagement.service.EnseignantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {

    private final EnseignantService service;

    public EnseignantController(EnseignantService service) {
        this.service = service;
    }

    @GetMapping
    public List<Enseignant> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Enseignant getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enseignant create(@Valid @RequestBody Enseignant objet) {
        return service.save(objet);
    }

    @PutMapping("/{id}")
    public Enseignant update(@PathVariable Long id, @Valid @RequestBody Enseignant objet) {
        return service.update(id, objet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
