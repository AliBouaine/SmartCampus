import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Course {
  id?: number;
  title: string;
  description?: string;
}

export interface ChatResponse {
  response: string;
}

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private BASE_URL = 'http://localhost:8083/courses';

  constructor(private http: HttpClient) {}

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.BASE_URL);
  }

  searchCourses(title: string): Observable<Course[]> {
    const params = new HttpParams().set('title', title);
    return this.http.get<Course[]>(`${this.BASE_URL}/search`, { params });
  }

  addCourse(course: Course): Observable<Course> {
    return this.http.post<Course>(this.BASE_URL, course);
  }

  updateCourse(id: number, course: Course): Observable<Course> {
    return this.http.put<Course>(`${this.BASE_URL}/${id}`, course);
  }

  deleteCourse(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }

  sendChatMessage(message: string): Observable<ChatResponse> {
    return this.http.post<ChatResponse>(`${this.BASE_URL}/chatbot`, { message });
  }
}
