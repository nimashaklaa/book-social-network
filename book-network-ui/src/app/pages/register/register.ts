import {Component, inject, signal} from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ApiConfiguration} from '../../services/api-configuration';
import {AuthenticationResponse} from '../../services/models/authentication-response';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  registrationRequest: RegistrationRequest ={
    email:'',
    firstname:'',
    lastname:'',
    password:'',
  }
  errorMessage = signal<Array<string>>([]);


  private router = inject(Router);
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);


  protected register() {
    this.errorMessage.set([]);
    this.http.post<AuthenticationResponse>(
      `${this.apiConfig.rootUrl}/auth/register`,
      this.registrationRequest
    ).subscribe({
      next: async (response) => {
        await this.router.navigate(['activate-account']);
      },
      error: (err) => {
        const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
        this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
      }
    });
  }

  protected async login() {
    await this.router.navigate(['/login']);
  }
}
