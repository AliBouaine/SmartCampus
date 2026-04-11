package com.example.course.service.Controller;

import com.example.course.service.Entity.Course;
import com.example.course.service.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping
    public List<Course> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Course one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Course add(@RequestBody Course c) {
        return service.add(c);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id,
                         @RequestBody Course c) {
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

