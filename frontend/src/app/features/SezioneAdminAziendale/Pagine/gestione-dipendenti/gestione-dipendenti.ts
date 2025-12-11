import { Component } from '@angular/core';
import { FormGestioneDipendentiAdminAziendale } from '@features/SezioneAdminAziendale/Componenti/form-gestione-dipendenti-admin-aziendale/form-gestione-dipendenti-admin-aziendale';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {DipendentiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dipendenti-service';
@Component({
  selector: 'app-gestione-dipendenti',
  standalone: true,
  imports: [FormGestioneDipendentiAdminAziendale],
  templateUrl: './gestione-dipendenti.html',
  styleUrl: './gestione-dipendenti.css'
})
export class GestioneDipendentiComponent {

  constructor(private service: DipendentiService) { }
  ngOnInit() {
    this.getDipendenti();
  }

  listaDipendentiAzienda: DipendenteDTO[] = [];


  getDipendenti() {
    this.service.getDipendenti().subscribe({
      next: (response: DipendenteDTO[]) => {
        this.listaDipendentiAzienda = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero dei dipendenti:', error);
      }
    })
  }


  rimuoviDipendente(idDipendente:number | undefined) {
    if (!confirm('Sei sicuro di voler eliminare questo dipendente?')) {
      return;
    }
    this.service.rimuoviDipendente(idDipendente).subscribe({
      next: (response) => {
        this.getDipendenti();
      },
      error: (error) => {
        console.error('Errore durante la rimozione del dipendente:', error);
      }
    });
  }


}
