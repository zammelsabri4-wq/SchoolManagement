package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Inscription;
import com.example.schoolmanagement.service.InscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionController {

    private final InscriptionService service;

    public InscriptionController(InscriptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Inscription> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Inscription getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inscription create(@Valid @RequestBody Inscription objet) {
        return service.save(objet);
    }

    @PutMapping("/{id}")
    public Inscription update(@PathVariable Long id, @Valid @RequestBody Inscription objet) {
        return service.update(id, objet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
