import {Component, inject, signal} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ApiConfiguration} from '../../services/api-configuration';
import {CodeInputModule} from 'angular-code-input';

@Component({
  selector: 'app-activate-account',
  imports: [
    CodeInputModule
  ],
  templateUrl: './activate-account.html',
  styleUrl: './activate-account.scss',
})
export class ActivateAccount {
  message = signal<string>('');
  isOkay = signal<boolean>(true);
  submitted = signal<boolean>(false);

  private router = inject(Router);
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  protected onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  protected async redirectedToLogin() {
    await this.router.navigate(['/login']);
  }

  private confirmAccount(token: string) {
    this.http.get(`${this.apiConfig.rootUrl}/auth/activate-account?token=${token}`).subscribe({
      next: async () => {
        this.message.set('Account activated successfully');
        this.isOkay.set(true);
        this.submitted.set(true);
      },
      error: (err) => {
        const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
        this.message.set(body?.error ?? 'Activation failed');
        this.isOkay.set(false);
        this.submitted.set(true);
      }
    });
  }
}
