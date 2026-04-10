package com.example.Formation.certificat.service.Entity;

import lombok.Data;

@Data
public class EducationEvent {

    private String type;
    private String message;

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

    public EducationEvent(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public EducationEvent() {
    }
}