package com.example.note.service.Controller;

import com.example.note.service.Entity.Note;
import com.example.note.service.Service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService service;

    @GetMapping
    public List<Note> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Note one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Note add(@RequestBody Note n) {
        return service.add(n);
    }

    @PutMapping("/{id}")
    public Note update(@PathVariable Long id,
                       @RequestBody Note n) {
        return service.update(id, n);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
