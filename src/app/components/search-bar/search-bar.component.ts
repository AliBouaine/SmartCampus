import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService, Course } from '../../services/course.service';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent {
  @Output() results = new EventEmitter<Course[]>();
  query = '';

  constructor(private courseService: CourseService) {}

  handleSearch(): void {
    if (this.query.trim() === '') {
      this.courseService.getAllCourses().subscribe(data => this.results.emit(data));
    } else {
      this.courseService.searchCourses(this.query).subscribe(data => this.results.emit(data));
    }
  }
}
