import { Component } from '@angular/core';
import { CourseListComponent } from './components/course-list/course-list.component';
import { ChatbotComponent } from './components/chatbot/chatbot.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CourseListComponent, ChatbotComponent],
  template: `
    <app-course-list />
    <app-chatbot />
  `
})
export class AppComponent {}
