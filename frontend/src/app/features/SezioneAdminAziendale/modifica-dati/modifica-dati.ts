import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormBackground } from '@shared/form-background/form-background';
import {
  FormModificaDatiAdminAziendale
} from '@shared/form-modifica-dati-admin-aziendale/form-modifica-dati-admin-aziendale';

@Component({
  selector: 'app-modifica-dati',
  standalone: true,
  imports: [
    FormsModule,
    FormModificaDatiAdminAziendale
  ],
  templateUrl: './modifica-dati.html',
  styleUrl: './modifica-dati.css'
})
export class ModificaDatiComponent {}
