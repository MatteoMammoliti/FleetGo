import {Component, inject} from '@angular/core';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import{FormBuilder} from '@angular/forms';
import {Router} from '@angular/router';
import {FormAutenticazione} from '@shared/form-autenticazione/form-autenticazione';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormAutenticazione
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})

export class LoginComponent {
  private fb=inject(FormBuilder) ;
  private router=inject(Router) ;


  errorePassword: string="";
  erroreEmail: string="";
  erroreCredenziali:string="";

  loginForm = this.fb.group({
  email: ['', [Validators.required, Validators.email]],
  password: ['', [Validators.required]]
})

  onSubmit() {
    if(this.loginForm.get('email')?.invalid){
      this.erroreEmail = "Email non valida";
      this.errorePassword = "";
      return;
    }
    else if(this.loginForm.get('password')?.invalid){
      this.errorePassword = "Password non valida";
      this.erroreEmail = "";
      return;
  }
    this.errorePassword="";
    this.erroreEmail="";

    const email = this.loginForm.value.email!;
    const password = this.loginForm.value.password!;
  }
}
