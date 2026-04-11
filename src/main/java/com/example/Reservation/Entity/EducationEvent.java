package com.example.Reservation.Entity;

import lombok.Data;

@Data
public class EducationEvent {

    private String type;
    private String message;

    // Getters et Setters explicites (obligatoires pour Jackson + RabbitMQ)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}