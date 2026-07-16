import {Component, inject} from '@angular/core';
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
  message:string = '';
  isOkay:boolean = true;
  submitted:boolean = false;

  private router = inject(Router);
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  protected onCodeCompleted(token: string) {

  }

  protected async redirectedToLogin() {
    await this.router.navigate(['/login']);
  }
}
