package com.example.course.service.Repository;

import com.example.course.service.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Recherche par titre (insensible à la casse)
    List<Course> findByTitleContainingIgnoreCase(String title);
}
