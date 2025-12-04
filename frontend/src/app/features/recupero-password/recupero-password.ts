import { Component } from '@angular/core';
import {FormRecuperoPassword} from '@shared/form-recupero-password/form-recupero-password';

@Component({
  selector: 'app-recupero-password',
  imports: [
    FormRecuperoPassword
  ],
  templateUrl: './recupero-password.html',
  styleUrl: './recupero-password.css',
})
export class RecuperoPassword {}
