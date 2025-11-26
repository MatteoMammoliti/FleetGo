import {Component, inject} from '@angular/core';
import {ReactiveFormsModule, Validators} from '@angular/forms';
import{FormBuilder} from '@angular/forms';
import {AuthService} from '@core/services/authService';
import {routes} from '../../app.routes';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {
  private fb=inject(FormBuilder) ;
  private authService=inject(AuthService) ;
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
    this.authService.login(email, password).subscribe({
      next: result => {
        this.router.navigate(['/dashboard']);
      },
      error: error => {
        this.erroreEmail = "Email o password non valida";
      }
    })



  }
}
