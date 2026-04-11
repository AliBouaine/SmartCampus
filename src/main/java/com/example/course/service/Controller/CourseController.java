package com.example.course.service.Controller;

import com.example.course.service.Entity.Course;
import com.example.course.service.Service.ChatbotService;
import com.example.course.service.Service.CourseService;
import com.example.course.service.dto.ChatbotRequest;
import com.example.course.service.dto.ChatbotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "http://localhost:3000") // ✅ autoriser React
public class CourseController {

    @Autowired
    private CourseService service;

    @Autowired
    private ChatbotService chatbotService;

    @GetMapping
    public List<Course> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Course one(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ Recherche par titre
    @GetMapping("/search")
    public List<Course> search(@RequestParam String title) {
        return service.searchByTitle(title);
    }

    // ✅ Chatbot
    @PostMapping("/chatbot")
    public ChatbotResponse chatbot(@RequestBody ChatbotRequest request) {
        String response = chatbotService.getResponse(request.getMessage());
        return new ChatbotResponse(response);
    }

    @PostMapping
    public Course add(@RequestBody Course c) {
        return service.add(c);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody Course c) {
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}