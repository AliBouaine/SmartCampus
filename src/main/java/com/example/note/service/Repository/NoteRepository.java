package com.example.note.service.Repository;

import com.example.note.service.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository
        extends JpaRepository<Note, Long> {
}
