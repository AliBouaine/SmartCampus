package com.example.course.service.Service;

import com.example.course.service.Entity.Course;
import com.example.course.service.Messaging.CourseProducer;
import com.example.course.service.Repository.CourseRepository;
import com.example.course.service.dto.CourseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final CourseProducer courseProducer;

    public CourseService(CourseRepository repository, CourseProducer courseProducer) {
        this.repository = repository;
        this.courseProducer = courseProducer;
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public Course getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Course add(Course c) {
        Course saved = repository.save(c);
        CourseDTO dto = new CourseDTO(saved.getId(), saved.getTitle(), saved.getDescription());
        courseProducer.sendCourse(dto);
        return saved;
    }

    public Course update(Long id, Course c) {
        Course existing = getById(id);
        existing.setTitle(c.getTitle());
        existing.setDescription(c.getDescription());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}