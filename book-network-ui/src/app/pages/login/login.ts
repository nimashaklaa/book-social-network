import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthenticationRequest } from '../../services/models/authentication-request';
import { AuthenticationResponse } from '../../services/models/authentication-response';
import { ApiConfiguration } from '../../services/api-configuration';
import {Token} from '../../services/token/token';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

  autheRequest: AuthenticationRequest = { email: '', password: '' };
  errorMessage = signal<Array<string>>([]);

  private router = inject(Router);
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);
  private tokenService = inject(Token)

  protected login() {
    this.errorMessage.set([]);
    this.http.post<AuthenticationResponse>(
      `${this.apiConfig.rootUrl}/auth/authenticate`,
      this.autheRequest
    ).subscribe({
      next: async (response) => {
        this.tokenService.token = response.token as string;
        await this.router.navigate(['/books']);
      },
      error: (err) => {
        const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
        this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
      }
    });
  }

  protected async register() {
    await this.router.navigate(['/register']);
  }
}
