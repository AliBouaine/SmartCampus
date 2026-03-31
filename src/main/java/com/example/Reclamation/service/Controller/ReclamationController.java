package com.example.Reclamation.service.Controller;

import com.example.Reclamation.service.Entity.Reclamation;
import com.example.Reclamation.service.Service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService service;

    @GetMapping
    public List<Reclamation> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Reclamation one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Reclamation add(@RequestBody Reclamation r) {
        return service.add(r);
    }

    @PutMapping("/{id}")
    public Reclamation update(@PathVariable Long id,
                              @RequestBody Reclamation r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
