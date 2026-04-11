package com.example.course.service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
}
