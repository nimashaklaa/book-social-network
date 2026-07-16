import {Component, signal} from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  registrationRequest: RegistrationRequest ={
    email:'',
    firstname:'',
    lastname:'',
    password:'',
  }
  errorMessage = signal<Array<string>>([]);


  protected register() {

  }

  protected login() {

  }
}
