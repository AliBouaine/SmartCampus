package com.example.course.service.Repository;

import com.example.course.service.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository
        extends JpaRepository<Course, Long> {
}
