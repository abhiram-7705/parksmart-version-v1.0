import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ChatService {
  constructor(private http: HttpClient) {}

sendMessage(message: string): Observable<string> {
  return this.http.post<{ reply: string }>('/api/chat', { message }, 
    { withCredentials: true })
    .pipe(map(res => res.reply));
}
}