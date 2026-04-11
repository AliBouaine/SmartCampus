package com.example.note.service.Repository;

import com.example.note.service.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByEtudiantId(Long etudiantId);
}