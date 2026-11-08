package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Classe;
import com.example.schoolmanagement.service.ClasseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseService service;

    public ClasseController(ClasseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Classe> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Classe getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Classe create(@Valid @RequestBody Classe objet) {
        return service.save(objet);
    }

    @PutMapping("/{id}")
    public Classe update(@PathVariable Long id, @Valid @RequestBody Classe objet) {
        return service.update(id, objet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
