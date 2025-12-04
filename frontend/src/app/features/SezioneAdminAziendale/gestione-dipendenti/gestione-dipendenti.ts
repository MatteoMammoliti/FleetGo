import { Component } from '@angular/core';
import { FormGestioneDipendentiAdminAziendale } from '@shared/form-gestione-dipendenti-admin-aziendale/form-gestione-dipendenti-admin-aziendale';
@Component({
  selector: 'app-gestione-dipendenti',
  standalone: true,
  imports: [FormGestioneDipendentiAdminAziendale], 
  templateUrl: './gestione-dipendenti.html',
  styleUrl: './gestione-dipendenti.css'
})
export class GestioneDipendentiComponent {
}