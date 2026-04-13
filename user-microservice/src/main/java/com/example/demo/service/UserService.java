package com.example.demo.service;

import com.example.demo.config.RabbitConfig;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.event.UserCreatedEvent;   // ← Import local maintenant
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final RabbitTemplate rabbitTemplate;

    public UserDTO register(RegisterRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .photo(req.getPhoto())
                .build();

        User savedUser = repo.save(user);
        publishUserCreatedEvent(savedUser);

        return mapToDTO(savedUser);
    }

    public UserDTO login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return mapToDTO(user);
    }

    public List<UserDTO> getAll() {
        return repo.findAll().stream().map(this::mapToDTO).toList();
    }

    public UserDTO getById(String id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public User add(User user) {
        User saved = repo.save(user);
        publishUserCreatedEvent(saved);
        return saved;
    }

    private void publishUserCreatedEvent(User user) {
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getPhoto()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,      // "formation-certificat.exchange"
                RabbitConfig.ROUTING_KEY,   // "formation-certificat.routing"
                event
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.USER_ROUTING_KEY,
                event
        );

        System.out.println("📤 Événement USER_CREATED envoyé vers RabbitMQ pour : " + user.getUsername());
    }

    private UserDTO mapToDTO(User u) {
        return UserDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .role(u.getRole())
                .photo(u.getPhoto())
                .build();
    }
}