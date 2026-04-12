import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService, Course } from '../../services/course.service';
import { SearchBarComponent } from '../search-bar/search-bar.component';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule, FormsModule, SearchBarComponent],
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {
  courses: Course[] = [];
  loading = true;

  newTitle = '';
  newDesc = '';

  editingId: number | null = null;
  editTitle = '';
  editDesc = '';

  constructor(private courseService: CourseService) {}

  ngOnInit(): void {
    this.fetchCourses();
  }

  fetchCourses(): void {
    this.loading = true;
    this.courseService.getAllCourses().subscribe({
      next: data => { this.courses = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  handleAdd(): void {
    if (!this.newTitle.trim()) return;
    this.courseService.addCourse({ title: this.newTitle, description: this.newDesc }).subscribe(() => {
      this.newTitle = '';
      this.newDesc = '';
      this.fetchCourses();
    });
  }

  handleDelete(id: number): void {
    this.courseService.deleteCourse(id).subscribe(() => this.fetchCourses());
  }

  handleEditOpen(c: Course): void {
    this.editingId = c.id!;
    this.editTitle = c.title;
    this.editDesc = c.description || '';
  }

  handleEditSave(id: number): void {
    this.courseService.updateCourse(id, { title: this.editTitle, description: this.editDesc }).subscribe(() => {
      this.editingId = null;
      this.fetchCourses();
    });
  }

  handleEditCancel(): void {
    this.editingId = null;
  }

  onSearchResults(results: Course[]): void {
    this.courses = results;
  }
}
