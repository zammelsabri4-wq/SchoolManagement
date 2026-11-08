package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Cours;
import com.example.schoolmanagement.service.CoursService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CoursController {

    private final CoursService service;

    public CoursController(CoursService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cours> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Cours getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cours create(@Valid @RequestBody Cours objet) {
        return service.save(objet);
    }

    @PutMapping("/{id}")
    public Cours update(@PathVariable Long id, @Valid @RequestBody Cours objet) {
        return service.update(id, objet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
