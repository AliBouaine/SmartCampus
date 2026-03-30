package com.example.note.service.Service;

import com.example.note.service.Entity.Note;
import com.example.note.service.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository repo;

    public List<Note> getAll() {
        return repo.findAll();
    }

    public Note getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Note add(Note n) {
        return repo.save(n);
    }

    public Note update(Long id, Note n) {
        Note old = getById(id);
        if (old != null) {
            old.setEtudiant(n.getEtudiant());
            old.setNote(n.getNote());
            return repo.save(old);
        }
        return null;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
