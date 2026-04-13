package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDTO> all(){
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.delete(id);
    }
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable String id){
        return service.getById(id);
    }

    private final UserRepository repo;
    @PostMapping
    public User add(@RequestBody User user){
        return repo.save(user);
    }
}

