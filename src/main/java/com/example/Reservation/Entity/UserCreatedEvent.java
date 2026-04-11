package com.example.Reservation.Entity;

import java.io.Serializable;

public class UserCreatedEvent implements Serializable {

    private String id;
    private String username;
    private String email;
    private String role;
    private String photo;

    public UserCreatedEvent() {}

    public UserCreatedEvent(String id, String username, String email, String role, String photo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.photo = photo;
    }
    public UserCreatedEvent(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

//    // ✅ Nouveau constructeur avec 5 paramètres
//    public UserCreatedEvent(String id, String username, String email, String role, String photo) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.role = role;
//        this.photo = photo;
//    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    @Override
    public String toString() {
        return "UserCreatedEvent{id='" + id + "', username='" + username
                + "', email='" + email + "', role='" + role + "', photo='" + photo + "'}";
    }
}