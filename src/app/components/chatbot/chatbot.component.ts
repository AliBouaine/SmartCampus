import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from '../../services/course.service';

interface Message {
  from: 'user' | 'bot';
  text: string;
}

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent {
  open = false;
  input = '';
  typing = false;

  messages: Message[] = [
    { from: 'bot', text: "Bonjour ! Je suis l'assistant SmartCampus 👋\nPosez-moi vos questions sur les cours." }
  ];

  constructor(private courseService: CourseService) {}

  toggleOpen(): void {
    this.open = !this.open;
  }

  handleSend(): void {
    if (!this.input.trim()) return;
    this.messages.push({ from: 'user', text: this.input });
    const userInput = this.input;
    this.input = '';
    this.typing = true;

    this.courseService.sendChatMessage(userInput).subscribe({
      next: res => {
        setTimeout(() => {
          this.typing = false;
          this.messages.push({ from: 'bot', text: res.response });
        }, 600);
      },
      error: () => {
        this.typing = false;
        this.messages.push({ from: 'bot', text: '❌ Erreur de connexion au serveur.' });
      }
    });
  }

  handleKey(e: KeyboardEvent): void {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      this.handleSend();
    }
  }

  trackByIndex(index: number): number {
    return index;
  }
}
